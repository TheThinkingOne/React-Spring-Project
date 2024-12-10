import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { RouterProvider } from 'react-router-dom'
import root from './router/root'
import './index.css'; // TailwindCSS 파일 import


function App() {
  const [count, setCount] = useState(0)

  return (
    <RouterProvider router={root}/>
  )
}

export default App
