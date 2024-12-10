import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'; // TailwindCSS 파일 import

import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
