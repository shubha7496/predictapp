import React, { useState, useEffect } from "react";
import axios from "axios";
import Select from "react-select"; // <-- Install this via npm i react-select
import { Container, Form, Button, Table, Card, Row, Col } from "react-bootstrap";

const API_URL = "http://localhost:8083/api/budget";

const categoryOptions = [
  { value: "Petrol", label: "Petrol" },
  { value: "Groceries", label: "Groceries" },
  { value: "Mobile Recharge", label: "Mobile Recharge" },
  { value: "Clothes", label: "Clothes" },
  { value: "Electricity", label: "Electricity" },
  { value: "Room Rent", label: "Room Rent" },
  { value: "Personal Expenses", label: "Personal Expenses" },
  { value: "Misc", label: "Misc" },
];

function App() {
  const [items, setItems] = useState([]);
  const [type, setType] = useState("expense");
  const [categories, setCategories] = useState([]); // Multi-select
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [date, setDate] = useState("");
  const [summary, setSummary] = useState(null);
  const [prediction, setPrediction] = useState(null);

  useEffect(() => {
    fetchItems();
    fetchSummary();
  }, []);

  const fetchItems = async () => {
    const res = await axios.get(API_URL);
    setItems(res.data);
  };

  const fetchSummary = async () => {
    const res = await axios.get(`${API_URL}/summary`);
    setSummary(res.data);
  };

  const fetchPrediction = async () => {
    const res = await axios.get(`${API_URL}/prediction`);
    setPrediction(res.data);
  };

  const handleAddItem = async (e) => {
    e.preventDefault();
    // Loop through selected categories to add multiple items
    for (let cat of categories) {
      const newItem = {
        type,
        category: cat.value,
        amount: parseFloat(amount),
        description,
        date,
      };
      await axios.post(API_URL, newItem);
    }

    // Reset form
    setCategories([]);
    setAmount("");
    setDescription("");
    setDate("");
    fetchItems();
    fetchSummary();
  };

  const handleDelete = async (id) => {
    await axios.delete(`${API_URL}/${id}`);
    fetchItems();
    fetchSummary();
  };

  return (
    <Container className="my-4">
      <h1 className="text-center mb-4">Budget Tracker</h1>

      <Card className="mb-4 p-3">
        <Form onSubmit={handleAddItem}>
          <Row>
            <Col md={2}>
              <Form.Select value={type} onChange={(e) => setType(e.target.value)}>
                <option value="income">Income</option>
                <option value="expense">Expense</option>
              </Form.Select>
            </Col>
            <Col md={3}>
              <Select
                options={categoryOptions}
                isMulti
                value={categories}
                onChange={setCategories}
                placeholder="Select Categories"
              />
            </Col>
            <Col md={2}>
              <Form.Control
                placeholder="Amount"
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                required
              />
            </Col>
            <Col md={3}>
              <Form.Control
                placeholder="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </Col>
            <Col md={2}>
              <Form.Control
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                required
              />
            </Col>
            <Col md={12} className="mt-2">
              <Button type="submit">Add</Button>
            </Col>
          </Row>
        </Form>
      </Card>

      <Card className="mb-4 p-3">
        <h4>Budget Items</h4>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Type</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Description</th>
              <th>Date</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.id}>
                <td>{item.type}</td>
                <td>{item.category}</td>
                <td>{item.amount}</td>
                <td>{item.description}</td>
                <td>{item.date}</td>
                <td>
                  <Button variant="danger" size="sm" onClick={() => handleDelete(item.id)}>
                    Delete
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Card>

      {summary && (
        <Card className="mb-4 p-3">
          <h4>Budget Summary</h4>
          <p>Total Income: ₹{summary.totalIncome}</p>
          <p>Total Expenses: ₹{summary.totalExpenses}</p>
          <p>Remaining Budget: ₹{summary.remainingBudget}</p>
          <h5>Categories:</h5>
          <ul>
            {Object.entries(summary.categories).map(([cat, values]) => (
              <li key={cat}>
                {cat} - Income: ₹{values.income}, Expenses: ₹{values.expenses}
              </li>
            ))}
          </ul>
        </Card>
      )}

      <Card className="p-3 mb-4">
        <Button onClick={fetchPrediction}>Get Spending Prediction</Button>
        {prediction && (
          <div className="mt-3">
            <p>
              <strong>Prediction:</strong> {prediction.prediction}
            </p>
            {prediction.overspendDate && <p><strong>Overspend Date:</strong> {prediction.overspendDate}</p>}
            {prediction.daysRemaining && <p><strong>Days Remaining:</strong> {prediction.daysRemaining}</p>}
            <p><strong>Recommendation:</strong> {prediction.recommendation}</p>
          </div>
        )}
      </Card>
    </Container>
  );
}

export default App;
