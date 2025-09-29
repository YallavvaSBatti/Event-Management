import axios from "axios"

const API_BASE_URL = "http://localhost:8080/api"

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
})

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token")
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Auth service
export const authService = {
  login: (credentials) => api.post("/auth/login", credentials),
  register: (userData) => api.post("/auth/register", userData),
}

// Event service
export const eventService = {
  getAllEvents: () => api.get("/events"),
  getEventById: (id) => api.get(`/events/${id}`),
  getEventsByCategory: (category) => api.get(`/events/category/${category}`),
  getPopularEvents: () => api.get("/events/popular"),
  searchEvents: (query) => api.get(`/events/search?q=${query}`),

}

// Booking service
export const bookingService = {
  createBooking: (bookingData) => api.post("/bookings", bookingData),
  getUserBookings: () => api.get("/bookings/user"),
}

export default api
