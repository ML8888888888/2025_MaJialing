import React, { useState } from 'react';
import axios from 'axios';
import { Container, Form, Button, Table, Alert, FormCheck, Badge } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

// Standard coin denominations (must match backend)
const STANDARD_DENOMINATIONS = [0.01, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 50, 100, 1000];

function CoinChangeCalculator() {
  // State management
  const [targetAmount, setTargetAmount] = useState('');
  const [selectedDenoms, setSelectedDenoms] = useState(
    STANDARD_DENOMINATIONS.reduce((acc, denom) => ({ ...acc, [denom]: false }), {})
  );
  const [result, setResult] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Handle denomination selection changes
  const handleDenominationChange = (denomination, isChecked) => {
    setSelectedDenoms(prev => ({
      ...prev,
      [denomination]: isChecked
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setResult([]);
    setLoading(true);

    try {
      // Validate input
      const amount = parseFloat(targetAmount);
      if (isNaN(amount)) {
        throw new Error('Please enter a valid target amount');
      }
      if (amount < 0 || amount > 10000) {
        throw new Error('Amount must be between 0 and 10,000');
      }

      // Extract selected denominations
      const activeDenominations = Object.entries(selectedDenoms)
        .filter(([_, isSelected]) => isSelected)
        .map(([denom]) => parseFloat(denom));

      if (activeDenominations.length === 0) {
        throw new Error('Please select at least one denomination');
      }

      // Call backend API
      const response = await axios.post('http://13.250.40.174:8080/api/coin-change', {
        targetAmount: amount,
        availableDenominations: activeDenominations
      });

      setResult(response.data.coins);
    } catch (err) {
      setError(err.response?.data?.error || err.message);
    } finally {
      setLoading(false);
    }
  };

  // Render result table
  const renderResultTable = () => {
    const coinSummary = result.reduce((acc, coin) => {
      const existing = acc.find(item => item.coin === coin);
      if (existing) {
        existing.count++;
      } else {
        acc.push({ coin, count: 1 });
      }
      return acc;
    }, []).sort((a, b) => a.coin - b.coin);

    return (
      <div className="mt-4">
        <h3>
          Calculation Result 
          <Badge bg="secondary" className="ms-2">{result.length} coins</Badge>
        </h3>
        <Table striped bordered hover className="mt-3">
          <thead>
            <tr>
              <th>Count</th>
              <th>Denomination</th>
            </tr>
          </thead>
          <tbody>
            {coinSummary.map((item, index) => (
              <tr key={index}>
                <td>{item.count}</td>
                <td>{item.coin.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
        <div className="mt-2">
          <strong>Detailed sequence:</strong> 
          <div className="d-flex flex-wrap gap-1 mt-2">
            {result.map((coin, idx) => (
              <Badge key={idx} bg="info" className="fs-6">
                {coin.toFixed(2)}
              </Badge>
            ))}
          </div>
        </div>
      </div>
    );
  };

  return (
    <Container className="my-5">
      <h1 className="mb-4 text-center">Coin Change Calculator</h1>
      
      <Form onSubmit={handleSubmit} className="border p-4 rounded-3 shadow-sm">
        {/* Target amount input */}
        <Form.Group controlId="targetAmount" className="mb-4">
          <Form.Label className="fw-bold">Target Amount (0 - 10,000)</Form.Label>
          <Form.Control
            type="number"
            step="0.01"
            min="0"
            max="10000"
            value={targetAmount}
            onChange={(e) => setTargetAmount(e.target.value)}
            placeholder="e.g. 7.03"
            required
            className="py-2"
          />
        </Form.Group>

        {/* Denomination selection */}
        <Form.Group controlId="denominations" className="mb-4">
          <Form.Label className="fw-bold">Select Available Denominations</Form.Label>
          <div className="d-flex flex-wrap gap-3">
            {STANDARD_DENOMINATIONS.map(denom => (
              <div key={denom} className="form-check form-check-inline">
                <input
                  className="form-check-input"
                  type="checkbox"
                  id={`denom-${denom}`}
                  checked={selectedDenoms[denom] || false}
                  onChange={(e) => handleDenominationChange(denom, e.target.checked)}
                />
                <label className="form-check-label" htmlFor={`denom-${denom}`}>
                  {denom.toFixed(2)}
                </label>
              </div>
            ))}
          </div>
        </Form.Group>

        {/* Submit button */}
        <div className="text-center">
          <Button 
            variant="primary" 
            type="submit" 
            disabled={loading}
            className="px-4 py-2">
            {loading ? 'Calculating...' : 'Calculate'}
          </Button>
        </div>
      </Form>

      {/* Error alert */}
      {error && (
        <Alert variant="danger" className="mt-4">
          {error}
        </Alert>
      )}

      {/* Result display */}
      {result.length > 0 && renderResultTable()}
    </Container>
  );
}

export default CoinChangeCalculator;
