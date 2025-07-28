import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { toast } from 'react-hot-toast';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import AddEmployeeModal from './AddEmployeeModal';
import EditEmployeeModal from './EditEmployeeModal';
import "../Style/Employee.css";
import { getEmployeeList,deleteEmployee } from '../Services/EmployeeService';
import ExportButton from '../Utils/ExportButton';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import SidebarEmployee from './SideBarEmployee';
import DeleteIcon from '@mui/icons-material/Delete';
import {Container, Box, IconButton, Tooltip } from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import { showConfirmAlert } from '../Utils/alerts';
import UploadFileIcon from '@mui/icons-material/UploadFile'; 
import '../Style/ImportButton.css';
import ImportExcel from  "../Utils/ImportExcel";
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd'; 
import AssignAssetToEmployee from './AssignAssetToEmployee'; 
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
    const [openEditModal, setOpenEditModal] = useState(false);
    const [employees, setEmployees] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [filterValue, setFilterValue] = useState('');
    const [filteredRows, setFilteredRows] = useState([]);
    const [viewOnly, setViewOnly] = useState(false);
    const [showImportModal, setShowImportModal] = useState(false);
    const [exportType, setExportType] = useState('all');
    const [open, setOpenAddModal] = useState(false); 
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
        setExportType('all');
    };

    const filterByStatus = (status) => {
        setExportType(status);
        const filtered = employees.filter(employee => employee.status.toLowerCase() === status.toLowerCase());
        setFilteredRows(filtered);
    };

    const handleDelete = async (employee) => {
        const confirmDelete = await showConfirmAlert('Are you sure?', `Do you want to Delete this Employee? ${employee.empId}`);
         if (!confirmDelete) return;

          try {
    const success = await deleteEmployee(employee.empId);
            if (success) {
                toast.success(`Deleted the Employee with ID: ${employee.empId}`); 
                fetchEmployees();
            }
        } catch (error) {
            console.error('Error deleting Employee:', error);
            toast.error(`Cannot Delete the Employee with ID: ${employee.empId}`); 
        }
        console.log(`Delete employee with ID: ${employee.empId}`);
    };
    const [openAssignAssetDialog, setOpenAssignAssetDialog] = useState(false);
    const [selectedAssignEmployee, setSelectedAssignEmployee] = useState(null);
        
    const handleOpenAssignAssets = (employee) => {
        setSelectedAssignEmployee(employee);
        setOpenAssignAssetDialog(true);
        };

    const columns = [
        { field: 'name', headerName: 'Name', width: 190, sortable: true },
        { field: 'mail', headerName: 'Email', width: 280, sortable: true },
        { field: 'designation', headerName: 'Designation', width: 180, sortable: true },
        { field: 'department', headerName: 'Department', width: 140, sortable: true },
        { field: 'location', headerName: 'Location', width: 120, sortable: true },
        { field: 'status', headerName: 'Status', width: 80, sortable: true },
        {
            field: 'actions',
            headerName: 'Actions',
            width: 190,
            sortable: false,
            renderCell: (params) => (
                            <Box display="flex" alignItems="center">
                                <Tooltip title="View">
                                    <IconButton
                                        color="inherit"
                                        sx={{
                                            transition: 'transform 0.2s',
                                            '&:hover': {
                                                transform: 'scale(1.3)',
                                                color: 'primary.main',
                                                filter: 'drop-shadow(0 0 4px rgba(25, 118, 210, 0.8))',
                                            },
                                            mr: 1,
                                        }}
                                        onClick={() => handleOpenEditModal(params.row, true)}
                                    >
                                        <VisibilityIcon />
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title="Assign Asset">
                                    <IconButton
                                        color="inherit"
                                        sx={{
                                            transition: 'transform 0.2s',
                                            '&:hover': {
                                                transform: 'scale(1.3)',
                                                color: 'success.main',
                                                filter: 'drop-shadow(0 0 4px rgba(76, 175, 80, 0.8))',
                                            },
                                        }}
                                        onClick={() => handleOpenAssignAssets(params.row)}
                                    >
                                        <AssignmentIndIcon />
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title="Edit">
                                    <IconButton
                                        color="inherit"
                                        sx={{
                                            transition: 'transform 0.2s',
                                            '&:hover': {
                                                transform: 'scale(1.3)',
                                                color: 'secondary.main',
                                                filter: 'drop-shadow(0 0 4px rgba(156, 39, 176, 0.8))',
                                            },
                                            mr: 1,
                                        }}
                                        onClick={()=>handleOpenEditModal(params.row)}
                                    >
                                        <EditIcon />
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title="Delete">
                                    <IconButton
                                        color="inherit"
                                        sx={{
                                            transition: 'transform 0.2s',
                                            '&:hover': {
                                                transform: 'scale(1.3)',
                                                color: 'error.main',
                                                filter: 'drop-shadow(0 0 4px rgba(211, 47, 47, 0.8))',
                                            },
                                        }}
                                        onClick={()=>handleDelete(params.row)}
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </Tooltip>
                            </Box>
                        ),
        },
    ];

    return (
        <div style={{width:'185vh'}}>
            <main className={classes.content}>
                <Container maxWidth="lg">
                    <div className={classes.filterContainer}>
                        <SidebarEmployee onAddEmployee={handleOpenAddModal} filterByStatus={filterByStatus} resetFilters={resetFilters}/>


                        <div
                            style={{
                            display: 'flex',
                            flexWrap: 'nowrap',
                            alignItems: 'center',
                            justifyContent: 'center',
                            gap: '20px',             
                            width: '100%',
                            marginBottom: '16px',
                            }}>

                            <div className="import-button" onClick={() => setShowImportModal(true)}>
                                <span style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'white', }}>
                                    <UploadFileIcon style={{ fontSize: 20 }} /> Import Employee
                                    </span>
                            </div>

                            {showImportModal && (
                            <ImportExcel
                              importType="employee"
                              onClose={() => setShowImportModal(false)}
                              refreshList = {fetchEmployees}
                            />
                            )}
                          
                            <TextField
                                label="Search"
                                variant="outlined"
                                onChange={handleSearch}
                                value={filterValue}
                                sx={{
                                width: { xs: '100%', md: '85vw' },
                                maxWidth: '1000px',
                                marginTop: 3,
                                '& .MuiOutlinedInput-root': {
                                    background: '#ffffff',
                                    borderRadius: '15px',
                                    color: '#52c5d2ff',
                                    fontWeight: 500,
                                    boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                                    '& fieldset': { border: '0.5px solid transparent' },
                                    '&:hover fieldset': { border: '0.5px solid #1FCBEA' },
                                    '&.Mui-focused fieldset': {
                                    boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                                    fontSize: '20px',
                                    },
                                    '& input': {
                                    background: 'transparent',
                                    color: '#083A40',
                                    fontFamily: "'Racing Sans One', sans-serif",
                                    },
                                },
                                '& .MuiInputLabel-root': {
                                    color: '#083A40',
                                    fontFamily: "'Racing Sans One', sans-serif",
                                    letterSpacing: '3.0px',
                                    
                                },
                                '& .Mui-focused .MuiInputLabel-root': {
                                    color: '#083A40',
                                },
                                '& .MuiInputLabel-shrink': {
                                    transform: 'translate(18px, -30px) scale(1.0)',
                                    background: 'transparent',
                                    color: '#fff',
                                    padding: '0 6px',
                                },
                    
                                }}
                            />

                            <div className="export-button">
                                <ExportButton
                            		type="employee"
                                    status={exportType}
                                    filter={filterValue}
                                    filePrefix="Verinite"
                                    filteredRows={filteredRows}
                            		buttonLabel={
                              			<span style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'white',  }}>
                                		<FileDownloadIcon  />
                                		Export Employee
                              			</span>
                                    }
                                />
                            </div>

                        </div>
                        
                        

                    </div>

                    <div style={{ height: '65vh', width: '85vw', display: 'flex', flexDirection: 'column', alignItems: 'center', }}>
                        <div style={{ height: 350, marginLeft: '2%', width: '95%', flexGrow: 1 }}>
                            <DataGrid
                                rows={filteredRows}
                                columns={columns}
                                pageSize={5}
                                sx={{
                                    borderRadius: '16px',
                                    overflow: 'hidden',
                                    border: '2px solid #1FCBEA',
                                    boxShadow: '0 0 3px #6DE0FF, 0 0 4px #2BC4F3',
                                    fontFamily: "'Racing Sans One', sans-serif",
                                    color: '#083A40',
                                    '& .MuiDataGrid-columnHeaders': {
                                    background: 'linear-gradient(45deg, #6DE0FF, #2BC4F3)',
                                    color: '#083A40',
                                    fontSize: '16px',
                                    fontWeight: 700,
                                    },
                                    '& .MuiDataGrid-cell': {
                                    background: '#F0FBFF',
                                    color: '#083A40',
                                    fontSize: '15px',
                                    borderBottom: '1px solid #D0F0FF',
                                    },
                                    '& .MuiDataGrid-footerContainer': {
                                    background: 'linear-gradient(45deg, #6DE0FF, #2BC4F3)',
                                    color: '#083A40',
                                    fontWeight: 600,
                                    },
                                    '& .MuiDataGrid-row:hover': {
                                    backgroundColor: '#E0F9FF',
                                    },
                                    '& .MuiDataGrid-selectedRowCount': {
                                    color: '#083A40',
                                    },
                                    '& .MuiCheckbox-root': {
                                    color: '#083A40',
                                    },
                                }}
                            />

                        </div>
                        
                    </div>
                    
                </Container>
                <AddEmployeeModal
                    open={open}
                    handleClose={() => setOpenAddModal(false)}
                    refreshEmployeeList={fetchEmployees}
                    />
                {selectedEmployee && (
                    <EditEmployeeModal
                        open={openEditModal}
                        handleClose={handleClose}
                        employee={selectedEmployee}
                        refreshEmployeeList={fetchEmployees}
                        viewOnly={viewOnly}
                    />
                )}
                {selectedAssignEmployee && (
                    <AssignAssetToEmployee
                        open={openAssignAssetDialog}
                        onClose={() => setOpenAssignAssetDialog(false)}
                        employee={selectedAssignEmployee}
                        refresh={fetchEmployees}
                    />
                    )}


                
            </main>
        </div>
        
    );
}

export default Employee;