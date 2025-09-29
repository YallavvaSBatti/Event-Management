"use client";

import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { eventService } from "../services/api";
import rockConcertImg from "../assets/images/RockConcert.jpg";
import techConferenceImg from "../assets/images/techconferance.jpeg";
import comedyNightImg from "../assets/images/comedynight.jpg";
import foodFestivalImg from "../assets/images/foodfestival.jpg";
import artExhibitionImg from "../assets/images/artexhibition.jpg";

// Map event IDs to images
const eventImages = {
  1: rockConcertImg,
  2: techConferenceImg,
  3: comedyNightImg,
  4: foodFestivalImg,
  5: artExhibitionImg,
};

// EventCard component
const EventCard = ({ event }) => {
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      weekday: "short",
      year: "numeric",
      month: "short",
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

  return (
    <Link to={`/events/${event.id}`} style={{ textDecoration: "none" }}>
      <div
        className="card"
        style={{ cursor: "pointer", transition: "transform 0.2s" }}
        onMouseEnter={(e) => (e.currentTarget.style.transform = "translateY(-2px)")}
        onMouseLeave={(e) => (e.currentTarget.style.transform = "translateY(0)")}
      >
        <div className="mb-4">
          <img
            src={eventImages[event.id] || rockConcertImg}
            alt={event.title}
            style={{
              width: "100%",
              height: "200px",
              objectFit: "cover",
              borderRadius: "var(--radius)",
              marginBottom: "1rem",
            }}
          />
        </div>

        <div className="mb-2">
          <span
            className="text-sm"
            style={{
              backgroundColor: "var(--muted)",
              padding: "0.25rem 0.5rem",
              borderRadius: "0.25rem",
              color: "var(--muted-foreground)",
            }}
          >
            {event.category}
          </span>
        </div>

        <h3 className="text-xl font-semibold mb-2">{event.title}</h3>
        <p className="text-muted text-sm mb-4">{event.venue}</p>

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-muted">{formatDate(event.eventDate)}</p>
            <p className="text-lg font-semibold">{formatPrice(event.price)}</p>
          </div>
          <div className="text-sm text-muted">{event.availableSeats} seats left</div>
        </div>
      </div>
    </Link>
  );
};

// EventList component
const EventList = ({ searchTerm }) => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await eventService.getAllEvents();
        setEvents(response.data);
      } catch (err) {
        setError("Failed to load events");
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  // Filter events by search term
  const filteredEvents = events.filter((event) =>
    event.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <div className="container py-12">
        <div className="text-center">Loading events...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-12">
        <div className="text-center text-destructive">{error}</div>
      </div>
    );
  }

  return (
    <div className="container">
      {filteredEvents.length === 0 && (
        <div className="text-center text-muted">No events found.</div>
      )}

        <div className="grid grid-cols-1 md:grid-cols-1 lg:grid-cols-3 "
      >
        {filteredEvents.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
      {/* </marquee> */}
      
    </div>
  );
};

export default EventList;
