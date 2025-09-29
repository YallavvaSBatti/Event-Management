"use client";

import { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { eventService } from "../services/api";
import { useAuth } from "../context/AuthContext";
import rockConcertImg from "../assets/images/RockConcert.jpg";
import techConferenceImg from "../assets/images/techconferance.jpeg";
import comedyNightImg from "../assets/images/comedynight.jpg";
import foodFestivalImg from "../assets/images/foodfestival.jpg";
import artExhibitionImg from "../assets/images/artexhibition.jpg";

const EventDetail = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const response = await eventService.getEventById(id);
        setEvent(response.data);
      } catch (err) {
        setError("Failed to load event details");
      } finally {
        setLoading(false);
      }
    };

    fetchEvent();
  }, [id]);

  const handleBookNow = () => {
    if (!user) {
      navigate("/login");
      return;
    }
    navigate(`/booking/${id}`);
  };

  if (loading) {
    return (
      <div className="container py-12">
        <div className="text-center">Loading event details...</div>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="container py-12">
        <div className="text-center text-destructive">
          {error || "Event not found"}
        </div>
      </div>
    );
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(price);
  };

  const eventImages = {
    1: rockConcertImg,
    2: techConferenceImg,
    3: comedyNightImg,
    4: foodFestivalImg,
    5: artExhibitionImg,
  };

  return (
    <div className="container py-12">
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <img
            src={eventImages[event.id] || rockConcertImg} // fallback image
            alt={event.title}
            style={{
              width: "100%",
              height: "200px",
              objectFit: "cover",
              borderRadius: "var(--radius)",
              marginBottom: "1rem",
            }}
          />

          <div className="mb-4">
            <span
              className="text-sm"
              style={{
                backgroundColor: "var(--muted)",
                padding: "0.5rem 1rem",
                borderRadius: "var(--radius)",
                color: "var(--muted-foreground)",
              }}
            >
              {event.category}
            </span>
          </div>

          <h1 className="text-3xl font-bold mb-4">{event.title}</h1>

          <div className="mb-6">
            <h2 className="text-xl font-semibold mb-2">About this event</h2>
            <p className="text-muted leading-relaxed">
              {event.description ||
                "Join us for an amazing experience that you won't forget!"}
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="card">
              <h3 className="font-semibold mb-2">📅 Date & Time</h3>
              <p className="text-muted">{formatDate(event.eventDate)}</p>
            </div>

            <div className="card">
              <h3 className="font-semibold mb-2">📍 Venue</h3>
              <p className="text-muted">{event.venue}</p>
            </div>
          </div>
        </div>

        <div>
          <div className="card" style={{ position: "sticky", top: "2rem" }}>
            <h2 className="text-2xl font-bold mb-4">Book Your Tickets</h2>

            <div className="mb-6">
              <div className="flex items-center justify-between mb-2">
                <span className="text-lg">Ticket Price</span>
                <span className="text-2xl font-bold">
                  {formatPrice(event.price)}
                </span>
              </div>
              <p className="text-sm text-muted">
                {event.availableSeats} of {event.totalSeats} seats available
              </p>
            </div>

            <button
              onClick={handleBookNow}
              className="btn btn-primary w-full mb-4"
              style={{ width: "100%" }}
              disabled={event.availableSeats === 0}
            >
              {event.availableSeats === 0 ? "Sold Out" : "Book Now"}
            </button>

            <div className="text-center">
              <Link
                to="/"
                className="text-sm text-muted"
                style={{ textDecoration: "underline" }}
              >
                ← Back to all events
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventDetail;
