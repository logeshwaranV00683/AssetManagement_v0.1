import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Button, ButtonGroup } from '@mui/material';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import AddEmployeeModal from './AddEmployeeModal';
import EditEmployeeModal from './EditEmployeeModal';
import "../Style/Employee.css";
import { getEmployeeList } from '../Services/EmployeeService';
import ExportButton from './ExportButton';
import FileDownloadIcon from '@mui/icons-material/FileDownload';

const useStyles = makeStyles((theme) => ({
    content: {
        flexGrow: 1,
    },
    filterContainer: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginTop: '30px',
        marginBottom: theme.spacing(2),
    },
    buttonGroup: {
        display: 'flex',
        alignItems: 'center',
        gap: theme.spacing(1),
    },
    button: {
        margin: theme.spacing(0.5),
    },
}));

function Employee() {
    const classes = useStyles();
    const [openAddModal, setOpenAddModal] = useState(false);
    const [openEditModal, setOpenEditModal] = useState(false);
    const [employees, setEmployees] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [filterValue, setFilterValue] = useState('');
    const [filteredRows, setFilteredRows] = useState([]);
    const [viewOnly, setViewOnly] = useState(false);
    const [exportType, setExportType] = useState('all');
    const fetchEmployees = async () => {
        try {
            const data = await getEmployeeList();
            setEmployees(data);
            setFilteredRows(data);
        } catch (error) {
            console.error('Error fetching employees:', error);
        }
    };

    useEffect(() => {
        fetchEmployees();
    }, []);

    const handleOpenAddModal = () => {
        setOpenAddModal(true);
    };

    const handleOpenEditModal = (employee, isViewOnly = false) => {
        setSelectedEmployee(employee);
        setViewOnly(isViewOnly);
        setOpenEditModal(true);
    };

    const handleClose = () => {
        setOpenAddModal(false);
        setOpenEditModal(false);
        setSelectedEmployee(null);
        setViewOnly(false);
    };

    const handleSearch = (event) => {
        const value = event.target.value.toLowerCase();
        setFilterValue(value);
        const filtered = employees.filter(employee =>
            Object.values(employee).some(val =>
                String(val).toLowerCase().includes(value)
            )
        );
        setFilteredRows(filtered);
    };

    const resetFilters = () => {
        setFilterValue('');
        setFilteredRows(employees);
        setExportType('all');  // renamed here
    };

    const filterByStatus = (status) => {
        setExportType(status);  // renamed here
        const filtered = employees.filter(employee => employee.status.toLowerCase() === status.toLowerCase());
        setFilteredRows(filtered);
    };

    const handleDelete = (employeeId) => {
        // Implement your delete logic here
        console.log(`Delete employee with ID: ${employeeId}`);
    };

    const columns = [
        { field: 'name', headerName: 'Name', width: 200, sortable: true },
        { field: 'mail', headerName: 'Email', width: 300, sortable: true },
        { field: 'designation', headerName: 'Designation', width: 150, sortable: true },
        { field: 'department', headerName: 'Department', width: 120, sortable: true },
        { field: 'location', headerName: 'Location', width: 100, sortable: true },
        { field: 'status', headerName: 'Status', width: 80, sortable: true },
        {
            field: 'actions',
            headerName: 'Actions',
            width: 300,
            sortable: false,
            renderCell: (params) => (
                <div style={{ display: 'flex', gap: '10px' }}>
                    <Button
                        variant="contained"
                        className={classes.button}
                        onClick={() => handleOpenEditModal(params.row, true)}
                    >
                        View
                    </Button>
                    <Button
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        onClick={() => handleOpenEditModal(params.row)}
                    >
                        Edit
                    </Button>
                    <Button
                        variant="contained"
                        color="secondary"
                        className={classes.button}
                        onClick={() => handleDelete(params.row.id)}
                    >
                        Delete
                    </Button>

                </div>
            ),
        },
    ];

    return (
        <div>
            <main className={classes.content}>
                <Container maxWidth="lg">
                    <div className={classes.filterContainer}>
                        <Button
                            variant="contained"
                            color="primary"
                            style={{ marginRight: 10 }}
                            startIcon={<AddCircleOutlineIcon />}
                            onClick={handleOpenAddModal}
                        >
                            Add Employee
                        </Button>
                       
                        <TextField
                            label="Search"
                            variant="standard"
                            onChange={handleSearch}
                            value={filterValue}
                            style={{ width: 400 }}
                        />
                        <ButtonGroup variant="contained" className={classes.buttonGroup}>
                            <Button onClick={() => filterByStatus('active')} className={classes.button}>Active</Button>
                            <Button onClick={() => filterByStatus('inactive')} className={classes.button}>Inactive</Button>
                            <Button onClick={resetFilters} className={classes.button}>Reset</Button>
                        </ButtonGroup>
                    </div>
                    <div style={{ height: 400, width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                        <div style={{ height: 350, marginLeft: '2%', width: '95%', flexGrow: 1 }}>
                            <DataGrid
                                rows={filteredRows}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5, 10, 20]}
                            />
                        </div>
                        <div className="floating-button">
                          <ExportButton
                            type="employee"
                            status={exportType}
                            filter={filterValue}
                            filePrefix="Verinite"
                            filteredRows={filteredRows}
                            buttonLabel={
                              <span style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'white' }}>
                                <FileDownloadIcon style={{ fontSize: 20 }} />
                                {`Export ${exportType} Employees`}
                              </span>
                            }
                          />
                        </div>
                    </div>
                </Container>
                <AddEmployeeModal open={openAddModal} handleClose={handleClose} refreshEmployeeList={fetchEmployees} />
                {selectedEmployee && (
                    <EditEmployeeModal
                        open={openEditModal}
                        handleClose={handleClose}
                        employee={selectedEmployee}
                        refreshEmployeeList={fetchEmployees}
                        viewOnly={viewOnly}
                    />
                )}
            </main>
        </div>
    );
}

export default Employee;
