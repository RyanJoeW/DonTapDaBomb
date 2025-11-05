import { useState, useEffect, useMemo } from 'react'
import {
  useReactTable,
  getCoreRowModel,
  flexRender
} from '@tanstack/react-table'
import axios from 'axios'
import '../App.css' // eventueel aanpassen als je een eigen CSS-file gebruikt

function RegisterPage() {
  const [users, setUsers] = useState([])

  const [name, setName] = useState('')
  const [cash, setCash] = useState('')
  const [password, setPassword] = useState('')

  // Kolommen voor de tabel
  const columns = useMemo(
    () => [
      { header: 'UserId', accessorKey: 'userId' },
      { header: 'Name', accessorKey: 'name' },
      { header: 'Cash', accessorKey: 'cash' },
      { header: 'Password', accessorKey: 'password' },
    ],
    []
  )

  // React Table instance
  const table = useReactTable({
    data: users,
    columns,
    getCoreRowModel: getCoreRowModel(),
  })

  // Users ophalen
  const getAllUsers = async () => {
    try {
      const res = await axios.get('http://localhost:8080/users')
      setUsers(res.data)
    } catch (err) {
      console.error('Fout bij ophalen van users:', err)
    }
  }

  // User toevoegen
  const addUser = async () => {
    if (!name || !cash || !password) {
      alert('Please fill in all fields')
      return
    }

    const cashNum = parseFloat(cash)
    if (Number.isNaN(cashNum)) {
      alert('Cash must be a valid number')
      return
    }

    const newUser = { name, cash: cashNum, password }

    try {
      // Voeg user toe
      await axios.post('http://localhost:8080/users', newUser)

      // Refresh de tabel
      await getAllUsers()

      // Leeg de velden
      setName('')
      setCash('')
      setPassword('')
    } catch (err) {
      console.error('Error adding user:', err)
      alert('Failed to add user. Check console for details.')
    }
  }

  useEffect(() => {
    getAllUsers()
  }, [])

  return (
    <>
      <h2>Account aanmaken</h2>
      <div className="add-panel">
        <div className="addpaneldiv">
          <label htmlFor="name">Name</label> <br />
          <input
            className="addpanelinput"
            type="text"
            name="name"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <div className="addpaneldiv">
          <label htmlFor="cash">Cash</label> <br />
          <input
            className="addpanelinput"
            type="text"
            name="cash"
            id="cash"
            value={cash}
            onChange={(e) => setCash(e.target.value)}
          />
        </div>

        <div className="addpaneldiv">
          <label htmlFor="password">Password</label> <br />
          <input
            className="addpanelinput"
            type="password"
            name="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <button className="addBtn" onClick={addUser}>Add</button>
        <button
          className="cancelBtn"
          onClick={() => {
            setName('')
            setCash('')
            setPassword('')
          }}
        >
          Cancel
        </button>
      </div>

      {/* Tabel met alle users */}
      <table className="table">
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <th key={header.id}>
                  {flexRender(header.column.columnDef.header, header.getContext())}
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody>
          {table.getRowModel().rows.map((row) => (
            <tr key={row.id}>
              {row.getVisibleCells().map((cell) => (
                <td key={cell.id}>
                  {flexRender(cell.column.columnDef.cell ?? cell.getValue, cell.getContext())}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}

export default RegisterPage