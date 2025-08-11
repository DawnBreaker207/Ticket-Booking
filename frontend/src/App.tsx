
import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import AdminLayout from './pages/admin/AdminLayout';
import MoviePage from './pages/admin/Movie';


const App = () => {


  return (
    <div>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/admin" element={<AdminLayout />}>
          <Route path="movie" element={<MoviePage />} />
        </Route>
      </Routes>
    </div>
  )
}

export default App