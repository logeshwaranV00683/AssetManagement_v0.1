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
