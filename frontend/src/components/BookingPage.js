"use client"

import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { eventService, bookingService } from "../services/api"
import { useAuth } from "../context/AuthContext"

const BookingPage = () => {
  const { id } = useParams()
  const { user } = useAuth()
  const navigate = useNavigate()
  const [event, setEvent] = useState(null)
  const [loading, setLoading] = useState(true)
  const [booking, setBooking] = useState(false)
  const [seats, setSeats] = useState(1)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!user) {
      navigate("/login")
      return
    }

    const fetchEvent = async () => {
      try {
        const response = await eventService.getEventById(id)
        setEvent(response.data)
      } catch (err) {
        setError("Failed to load event details")
      } finally {
        setLoading(false)
      }
    }

    fetchEvent()
  }, [id, user, navigate])

  const handleBooking = async (e) => {
    e.preventDefault()
    setBooking(true)
    setError(null)

    try {
      const bookingData = {
        eventId: Number.parseInt(id),
        seatsBooked: seats,
        totalAmount: event.price * seats,
      }

      await bookingService.createBooking(bookingData)
      alert("Booking successful! Check your email for confirmation.")
      navigate("/")
    } catch (err) {
      setError(err.response?.data || "Booking failed. Please try again.")
    } finally {
      setBooking(false)
    }
  }

  if (loading) {
    return (
      <div className="container py-12">
        <div className="text-center">Loading booking details...</div>
      </div>
    )
  }

  if (error && !event) {
    return (
      <div className="container py-12">
        <div className="text-center text-destructive">{error}</div>
      </div>
    )
  }

  const formatPrice = (price) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(price)
  }

  const totalAmount = event ? event.price * seats : 0

  return (
    <div className="container py-12">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-center">Complete Your Booking</h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2">
            <div className="mb-8">
              <div className="flex items-center gap-4 mb-4">
                <div className="w-8 h-8 bg-primary text-primary-foreground rounded-full flex items-center justify-center font-semibold">
                  1
                </div>
                <h2 className="text-xl font-semibold">Select your tickets</h2>
              </div>

              {event && (
                <div className="card">
                  <div className="flex items-center gap-4 mb-4">
                    <img
                      src={event.imageUrl || "/placeholder.svg?height=80&width=80&query=event"}
                      alt={event.title}
                      style={{
                        width: "80px",
                        height: "80px",
                        objectFit: "cover",
                        borderRadius: "var(--radius)",
                      }}
                    />
                    <div>
                      <h3 className="font-semibold">{event.title}</h3>
                      <p className="text-muted text-sm">{event.venue}</p>
                      <p className="text-muted text-sm">{new Date(event.eventDate).toLocaleDateString()}</p>
                    </div>
                  </div>

                  <div
                    className="flex items-center justify-between p-4"
                    style={{ backgroundColor: "var(--muted)", borderRadius: "var(--radius)" }}
                  >
                    <div>
                      <p className="font-medium">General Admission</p>
                      <p className="text-sm text-muted">{formatPrice(event.price)} per ticket</p>
                    </div>
                    <div className="flex items-center gap-2">
                      <button
                        type="button"
                        onClick={() => setSeats(Math.max(1, seats - 1))}
                        className="w-8 h-8 rounded border border-border flex items-center justify-center"
                        style={{ backgroundColor: "var(--background)" }}
                      >
                        -
                      </button>
                      <span className="w-8 text-center">{seats}</span>
                      <button
                        type="button"
                        onClick={() => setSeats(Math.min(event.availableSeats, seats + 1))}
                        className="w-8 h-8 rounded border border-border flex items-center justify-center"
                        style={{ backgroundColor: "var(--background)" }}
                      >
                        +
                      </button>
                    </div>
                  </div>
                </div>
              )}
            </div>

            <div className="mb-8">
              <div className="flex items-center gap-4 mb-4">
                <div className="w-8 h-8 bg-primary text-primary-foreground rounded-full flex items-center justify-center font-semibold">
                  2
                </div>
                <h2 className="text-xl font-semibold">Confirm your details</h2>
              </div>

              <div className="card">
                <p className="mb-2">
                  <strong>Name:</strong> {user?.name}
                </p>
                <p className="mb-2">
                  <strong>Email:</strong> {user?.email}
                </p>
                <p className="text-sm text-muted">Booking confirmation will be sent to this email address.</p>
              </div>
            </div>

            <div>
              <div className="flex items-center gap-4 mb-4">
                <div className="w-8 h-8 bg-primary text-primary-foreground rounded-full flex items-center justify-center font-semibold">
                  3
                </div>
                <h2 className="text-xl font-semibold">Complete booking</h2>
              </div>

              {error && (
                <div className="mb-4 p-4 bg-destructive/10 border border-destructive/20 rounded text-destructive">
                  {error}
                </div>
              )}

              <form onSubmit={handleBooking}>
                <button type="submit" className="btn btn-primary w-full" disabled={booking} style={{ width: "100%" }}>
                  {booking ? "Processing..." : `Confirm Booking - ${formatPrice(totalAmount)}`}
                </button>
              </form>
            </div>
          </div>

          <div>
            <div className="card" style={{ position: "sticky", top: "2rem" }}>
              <h3 className="text-lg font-semibold mb-4">Order Summary</h3>

              {event && (
                <>
                  <div className="mb-4">
                    <div className="flex justify-between mb-2">
                      <span>Tickets ({seats}x)</span>
                      <span>{formatPrice(event.price * seats)}</span>
                    </div>
                  </div>

                  <div className="border-t border-border pt-4">
                    <div className="flex justify-between font-semibold text-lg">
                      <span>Total</span>
                      <span>{formatPrice(totalAmount)}</span>
                    </div>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default BookingPage
