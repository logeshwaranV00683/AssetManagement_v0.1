const token = localStorage.getItem('authToken');
const apiUrl = process.env.REACT_APP_API_URL;
console.log(process.env);

export const logIn = (formData) => {
  return fetch(`${apiUrl}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(formData),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .catch((error) => {
      console.error('Error during Sign In:', error);
      throw error;
    });
}

export const getEmployeeList = async() => {
    const url = `${apiUrl}/assetManager/v1/employee/employeelist`;
    try {
      const response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      let data = await response.json();
      let filteredData = data.map(employee => ({
        ...employee,
        id: employee.empId,
        name:employee.firstName + " " + employee.lastName
      }));
      console.log('dt', filteredData);
      return filteredData;
    } catch (error) {
      console.error('Error fetching employee list:', error);
      throw error;
    }
  };
  
 export const saveEmployee = async (employeeData) => {
    try {
      const response = await fetch(`${apiUrl}/assetManager/v1/employee/saveemployee`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(employeeData),
      });
      
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      return await response.json();
    } catch (error) {
      console.error('Error saving employee:', error);
      throw error;
    }
  };
  
  export const updateEmployee = async (empId, employee) => {
    console.log('json',  JSON.stringify(employee));
    try {
        const response = await fetch(`${apiUrl}/assetManager/v1/updateEmp/${empId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(employee),
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    } catch (error) {
        console.error('Error updating employee:', error);
        throw error;
    }
};