"use client"
import { useState, useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import { useAuth } from "../context/AuthContext"
import { eventService } from "../services/api"
import { FiSearch } from "react-icons/fi" // import search icon from react-icons

const Header = ({ onSearch }) => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [searchTerm, setSearchTerm] = useState("")
  const [searchResults, setSearchResults] = useState([])
  const [showResults, setShowResults] = useState(false)

  const handleLogout = () => {
    logout()
    navigate("/")
  }

  useEffect(() => {
    if (onSearch) {
      onSearch(searchTerm)
    }
  }, [searchTerm, onSearch])

  useEffect(() => {
    const fetchResults = async () => {
      if (!searchTerm.trim()) {
        setSearchResults([])
        return
      }

      try {
        const response = await eventService.searchEvents(searchTerm)
        setSearchResults(response.data || [])
      } catch (err) {
        console.error("Search failed", err)
      }
    }

    fetchResults()
  }, [searchTerm])

  const handleSelectResult = (eventId) => {
    navigate(`/events/${eventId}`)
    setSearchTerm("")
    setSearchResults([])
    setShowResults(false)
  }

  return (
    <header className="bg-white shadow-sm">
      <div className="container mx-auto">
        <nav className="flex items-center justify-between py-2 relative">
          <Link
            to="/"
            className="text-xl font-bold"
            style={{ textDecoration: "none", color: "var(--foreground)" }}
          >
            Event Connect
          </Link>

          <div className="relative"> 
            <FiSearch className="absolute top-2.5 left-3 text-gray-400" />
            <input
              type="text"
              placeholder="Search by title, venue, category..."
              className="border rounded px-10 w-100px py-1 w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onFocus={() => setShowResults(true)}
              onBlur={() => setTimeout(() => setShowResults(false), 200)}
            />

          </div>

          {/* <div className="hidden md:flex items-center gap-6"> */}
            {/* <Link to="/" style={{ textDecoration: "none", color: "var(--muted-foreground)" }}>
              Events
            </Link> */}
        
            {/* <Link to="/" style={{ textDecoration: "none", color: "var(--muted-foreground)" }}>
              Popular
            </Link> */}
          {/* </div> */}

          <div className="flex items-center gap-4">
            {user ? (
              <>
                <span className="text-sm text-muted">Welcome, {user.name}</span>
                <button onClick={handleLogout} className="btn btn-secondary">
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-secondary">
                  Login
                </Link>
                <Link to="/register" className="btn btn-primary">
                  Sign Up
                </Link>
              </>
            )}
          </div>
        </nav>
      </div>
    </header>
  )
}

export default Header
