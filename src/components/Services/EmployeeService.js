const token = localStorage.getItem("authToken");
const apiUrl = process.env.REACT_APP_API_URL;

export const getEmployeeList = async () => {
  const url = `${apiUrl}/assetManager/v1/employee/employeelist`;
  try {
    const response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Network response was not ok " + response.statusText);
    }
    let data = await response.json();
    let filteredData = data.map((employee) => ({
      ...employee,
      id: employee.empId,
      name: employee.firstName + " " + employee.lastName,
    }));
    console.log("dt", filteredData);
    return filteredData;
  } catch (error) {
    console.error("Error fetching employee list:", error);
    throw error;
  }
};

export const saveEmployee = async (employeeData) => {
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/employee/saveemployee`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(employeeData),
      }
    );

    if (!response.ok) {
      let errorData;
      const contentType = response.headers.get("content-type");

      if (contentType && contentType.includes("application/json")) {
        errorData = await response.json();
      } else {
        errorData = await response.text();
      }

      const error = new Error("Request failed");
      error.status = response.status;
      error.data = errorData;
      throw error;
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const updateEmployee = async (empId, employee) => {
  console.log("json", JSON.stringify(employee));
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/updateEmp/${empId}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(employee),
      }
    );

    if (!response.ok) {
      let errorData;
      const contentType = response.headers.get("content-type");

      if (contentType && contentType.includes("application/json")) {
        errorData = await response.json();
      } else {
        errorData = await response.text();
      }

      const error = new Error("Request failed");
      error.status = response.status;
      error.data = errorData;
      throw error;
    }

    const text = await response.text();
    try {
      return JSON.parse(text);
    } catch {
      return { message: text };
    }
  } catch (error) {
    console.error("Error updating employee:", error);
    throw error;
  }
};

export const deleteEmployee = async (empId) => {
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/deleteEmp/${empId}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      const errorText = await response.text();
      const error = new Error(errorText || "Failed to delete employee");
      error.status = response.status;
      throw error;
    }

    return true;
  } catch (error) {
    console.error("Error deleting employee:", error);
    throw error;
  }
};

export const getLocations = async () => {
  const url = `${apiUrl}/assetManager/v1/employee/getAllUniqueLocation`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("Employee Location API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching Locations:", error);
    return [];
  }
};

export const getDeparatment = async () => {
  const url = `${apiUrl}/assetManager/v1/employee/getAllUniqueDepartment`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("Employee Department API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching Department:", error);
    return [];
  }
};

export const getDesignation = async () => {
  const url = `${apiUrl}/assetManager/v1/employee/getAllUniqueDesignation`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("Employee Designation API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching Designations:", error);
    return [];
  }
};

export const getEmployeeById = async (empId) => {
  const url = `${apiUrl}/assetManager/v1/getEmployee/${empId}`;
  try {
    const response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Network response was not ok " + response.statusText);
    }

    const employee = await response.json();
    const formattedEmployee = {
      ...employee,
      id: employee.empId,
      name: employee.firstName + " " + employee.lastName,
    };

    return formattedEmployee;
  } catch (error) {
    console.error("Error fetching employee:", error);
    throw error;
  }
};
