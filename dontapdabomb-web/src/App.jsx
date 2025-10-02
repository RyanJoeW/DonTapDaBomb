import { useState, useEffect, useMemo } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {
  useReactTable,
  getCoreRowModel,
  flexRender
} from '@tanstack/react-table'
import axios from 'axios'

function App() {
  const [users, setUsers] = useState([])

  // Kolommen definiëren
  const columns = useMemo(
    () => [
      { header: 'UserId', accessorKey: 'userId' },
      { header: 'Name', accessorKey: 'name' },
      { header: 'Cash', accessorKey: 'cash' },
      { header: 'Password', accessorKey: 'password' },
    ],
    []
  )

  // Table instance maken
  const table = useReactTable({
    data: users,
    columns,
    getCoreRowModel: getCoreRowModel(),
  })

  // Users ophalen
  const getAllUsers = () => {
    axios.get('http://localhost:8080/users').then((res) => {
      console.log(res.data)
      setUsers(res.data)
    })
  }

  useEffect(() => {
    getAllUsers()
  }, [])

  return (
    <>
      <div className="main-container">
        <h3>dontapdabomb beta webaplicatie lezzz goooo</h3>
        <div className="add-panel">
          <div className="addpaneldiv">
            <label htmlFor="name">Name</label> <br />
            <input className="addpanelinput" type="text" name="name" id="name" />
          </div>
          <div className="addpaneldiv">
            <label htmlFor="cash">Cash</label> <br />
            <input className="addpanelinput" type="text" name="cash" id="cash" />
          </div>
          <div className="addpaneldiv">
            <label htmlFor="password">Password</label> <br />
            <input
              className="addpanelinput"
              type="text"
              name="password"
              id="password"
            />
          </div>
          <button className="addBtn">Add</button>
          <button className="cancelBtn">Cancel</button>
        </div>
        <input
          className="searchinput"
          type="search"
          name="inputsearch"
          id="inputsearch"
          placeholder="search User Here"
        />
      </div>

      {/* Table renderen */}
      <table className="table">
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <th key={header.id}>
                  {header.isPlaceholder
                    ? null
                    : flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
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

export default App