import { useState, useEffect } from "react"
import { Link } from "react-router-dom"

const Category = () => {
  const [events, setEvents] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [search, setSearch] = useState("")

  useEffect(() => {
    const fetchAllEvents = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/events`)
        if (!response.ok) throw new Error("Failed to fetch")
        const data = await response.json()
        setEvents(data)
      } catch (err) {
        setError("Failed to load events")
      } finally {
        setLoading(false)
      }
    }
    fetchAllEvents()
  }, [])

  useEffect(() => {
    const handler = e => setSearch(e.detail)
    window.addEventListener("searchEvent", handler)
    return () => window.removeEventListener("searchEvent", handler)
  }, [])

  const filteredEvents = events.filter(event =>
    [event.title, event.venue, event.description, event.category, String(event.price)]
      .some(field => field?.toLowerCase().includes(search.toLowerCase()))
  )

  if (loading) return <div className="container py-12"><div className="text-center">Loading events...</div></div>
  if (error) return <div className="container py-12"><div className="text-center text-destructive">{error}</div></div>

  return (
    <div className="container py-12">
      <div className="mb-8">
        <h2 className="text-2xl font-bold mb-2">All Events</h2>
        <p className="text-muted text-lg">Browse all events!</p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredEvents.map((event) => (
          <Link key={event.id} to={`/events/${event.id}`}>
            <div className="card">
              <img
                src={event.imageUrl || "/placeholder.svg?height=200&width=300&query=event"}
                alt={event.title}
                style={{
                  width: "100%",
                  height: "200px",
                  objectFit: "cover",
                  borderRadius: "var(--radius)",
                  marginBottom: "1rem",
                }}
              />
              <h3 className="text-xl font-semibold mb-2">{event.title}</h3>
              <p className="text-muted text-sm mb-4">{event.venue}</p>
              <p className="text-sm text-muted">{new Date(event.eventDate).toLocaleString()}</p>
              <p className="text-lg font-semibold">${event.price}</p>
              <div className="text-sm text-muted">{event.availableSeats} seats left</div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}

export default Category