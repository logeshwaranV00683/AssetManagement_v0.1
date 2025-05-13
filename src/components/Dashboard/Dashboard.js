import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';
import { Container, Card, CardContent, Typography, Box } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';

const useStyles = makeStyles((theme) => ({
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
  pieContainer: {
    display: 'flex',
    justifyContent: 'space-around',
    marginTop: theme.spacing(3),
    marginLeft:'25px'
  },
  pieCard: {
    width: '45%',
  },
  label: {
    textAlign: 'center',
    color: 'grey',
    marginTop: theme.spacing(2),
  },
}));

function Dashboard() {
  const classes = useStyles();
  const data = [
    { name: 'Stock', value: 23 },
    { name: 'Assigned', value: 104 },
  ];

  const data2 = [
    { name: 'Stock', value: 26 },
    { name: 'Assigned', value: 146 },
  ];

  const COLORS = ['#3366CC', '#DC3912'];
  const columns = [
    { field: 'id', headerName: 'ID', width: 90 },
    { field: 'assetName', headerName: 'Asset name', width: 200 },
    { field: 'serialNumber', headerName: 'Serial Number', width: 200 },
    { field: 'location', headerName: 'Location', width: 150 },
    { field: 'model', headerName: 'MODL', width: 150 }, // Adjusted column name
    { field: 'assign', headerName: 'ASSIGN', width: 150 }, // Adjusted column name
  ];

  const rows = [
    { id: 1, assetName: 'Laptop', serialNumber: 'SN001', location: 'Room A', model: 'Model X', assign: 'Assign X' },
    { id: 2, assetName: 'Desktop', serialNumber: 'SN002', location: 'Room B', model: 'Model Y', assign: 'Assign Y' },
    { id: 3, assetName: 'Printer', serialNumber: 'SN003', location: 'Room C', model: 'Model Z', assign: 'Assign Z' },
    // Add more rows as needed
  ];
  const [filterModel, setFilterModel] = useState({ items: [] });
  const [filterValue, setFilterValue] = useState('');

  const [filteredRows, setFilteredRows] = useState(rows);

  const handleSearch = (event) => {
    const { value } = event.target;
    const lowercaseValue = value.toLowerCase();
    setFilterValue(lowercaseValue);

    const filteredRows = rows.filter(row =>
      row.assetName.toLowerCase().includes(lowercaseValue) ||
      row.serialNumber.toLowerCase().includes(lowercaseValue) ||
      row.location.toLowerCase().includes(lowercaseValue) ||
      row.model.toLowerCase().includes(lowercaseValue) ||
      row.assign.toLowerCase().includes(lowercaseValue)
    );

    setFilteredRows(filteredRows);
  };

  return (
    <div>
      <main className={classes.content}>
        <Container maxWidth="lg">
          <div className={classes.pieContainer}>
            <Card className={classes.pieCard}>
              <CardContent>
                <div className={classes.label}>
                  <Typography variant="h6">Laptop: {data[0].value}/{data[1].value}</Typography>
                  <Typography variant="h6">CHENNAI</Typography>

                </div>
                <div style={{ height: 250 }}>
                  <ResponsiveContainer>
                    <PieChart>

                      <Pie
                        dataKey="value"
                        data={data}
                        cx="50%"
                        cy="50%"
                        innerRadius={0}
                        outerRadius={80}
                        fill="#8884d8"
                        label
                      >
                        {data.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]}
                          />
                        ))}
                      </Pie>
                      <Tooltip />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </div>

              </CardContent>
            </Card>
            <Card className={classes.pieCard}>
              <CardContent>
                <div className={classes.label}>
                  <Typography variant="h6">Laptop: {data2[0].value}/{data2[1].value}</Typography>
                  <Typography variant="h6">PUNE</Typography>
                </div>
                <div style={{ height: 250 }}>
                  <ResponsiveContainer>
                    <PieChart>
                      <Pie
                        dataKey="value"
                        data={data2}
                        cx="50%"
                        cy="50%"
                        innerRadius={0}
                        outerRadius={80}
                        fill="#8884d8"
                        label
                      >
                        {data.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                      </Pie>
                      <Tooltip />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </div>

              </CardContent>
            </Card>
          </div>
          <div style={{ height: 400, width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Box mb={1}>
              <TextField
                label="Search"
                variant="standard"
                onChange={handleSearch}
                style={{ width: 400, marginTop: 10}} // Adjust the width and margin as needed
              />
            </Box>
            <div style={{ height: 350, marginLeft: '2%', width: '95%',flexGrow: 1 }}>
              <DataGrid
                rows={filteredRows}
                columns={columns}
                pageSize={5}
                rowsPerPageOptions={[5, 10, 20]}
                // checkboxSelection
                filterModel={filterModel}
                onFilterModelChange={(model) => setFilterModel(model)}
              />
            </div>
          </div>

        </Container>
      </main>
    </div>
  );
}

export default Dashboard;
