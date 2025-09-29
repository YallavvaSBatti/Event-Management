import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import EventList from "./components/EventList";
import EventDetail from "./components/EventDetail";
import BookingPage from "./components/BookingPage";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import { AuthProvider } from "./context/AuthContext";
import "./index.css";
import Popular from "./components/Popular";
import Category from "./components/Category";
import { useState } from "react";
import DistanceCalculator from "./components/DistanceCalculator";

function App() {
  const [searchTerm, setSearchTerm] = useState(""); // Track search input

  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header onSearch={(term) => setSearchTerm(term)} />

            {/* <DistanceCalculator/> */}
          <main>
            <Routes>
              <Route
                path="/"
                element={<EventList searchTerm={searchTerm} />}
              />
              <Route path="/events/:id" element={<EventDetail />} />
              <Route path="/booking/:id" element={<BookingPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/popular" element={<Popular />} />
              <Route path="/category" element={<Category />} />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
