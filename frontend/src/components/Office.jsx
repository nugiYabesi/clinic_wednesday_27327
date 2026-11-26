import { useEffect, useState } from 'react';
import axios from 'axios';
import LibraryButton from './LibraryButton.jsx';

const API = 'http://localhost:8081/api/offices';

function Office() {
  const [offices, setOffices] = useState([]);
  const [searchText, setSearchText] = useState('');

  async function loadOffices() {
    try {
      const response = await axios.get(API + '/all');
      setOffices(response.data);
    } catch (error) {
      alert(error.response?.data || error.message);
    }
  }

  useEffect(() => {
    loadOffices();
  }, []);

  async function deleteOffice(officeNumber) {
    try {
      await axios.delete(API + '/delete/' + officeNumber);
      setOffices(offices.filter((office) => office.officeNumber !== officeNumber));
    } catch (error) {
      alert(error.response?.data || error.message);
    }
  }

  const visibleOffices = offices.filter((office) =>
    office.name.toLowerCase().includes(searchText.toLowerCase()) ||
    String(office.officeNumber).toLowerCase().includes(searchText.toLowerCase())
  );

  return (
    <main style={{ color: 'black', padding: '20px' }}>
      <h1>Available Offices</h1>

      <label htmlFor="office-search">Search by office name or number:</label>
      <br />
      <input
        id="office-search"
        type="text"
        value={searchText}
        onChange={(event) => setSearchText(event.target.value)}
        placeholder="Enter a name or number"
        style={{ padding: '8px', margin: '8px 0 20px' }}
      />

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid gray', padding: '8px' }}>Office Number</th>
            <th style={{ border: '1px solid gray', padding: '8px' }}>Office Name</th>
            <th style={{ border: '1px solid gray', padding: '8px' }}>Action</th>
          </tr>
        </thead>
        <tbody>
          {visibleOffices.map((office) => (
            <tr key={office.id}>
              <td style={{ border: '1px solid gray', padding: '8px' }}>{office.officeNumber}</td>
              <td style={{ border: '1px solid gray', padding: '8px' }}>{office.name}</td>
              <td style={{ border: '1px solid gray', padding: '8px' }}>
                <button onClick={() => deleteOffice(office.officeNumber)}>Delete</button>{' '}
                <button type="button">Update</button>
              </td>
            </tr>
          ))}
          {visibleOffices.length === 0 && (
            <tr>
              <td colSpan="3" style={{ padding: '8px' }}>No offices found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </main>
  );
}

export default Office;
