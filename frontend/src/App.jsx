import LibraryHeader from './components/LibraryHeader.jsx'
import LibraryFooter from './components/LibraryFooter.jsx'
import LibraryLogin from './components/LibraryLogin.jsx'
import { Route, Routes } from 'react-router'
import Office from './components/Office.jsx'

import LibraryMain from './components/LibraryMain.jsx'

function App() {
  return (
    <>
      
      

      <Routes>
        <Route path="/" element={<LibraryLogin/>} />
        <Route path="/office" element={<Office/>} />
        <Route path="/main" element={<LibraryMain/>} />

      </Routes>

      <LibraryFooter />
    </>
  )
}

export default App
