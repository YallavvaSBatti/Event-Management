"use client"

import { createContext, useContext, useState, useEffect } from "react"
import { authService } from "../services/api"

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem("token")
    const userData = localStorage.getItem("user")

    if (token && userData) {
      setUser(JSON.parse(userData))
    }
    setLoading(false)
  }, [])

  const login = async (credentials) => {
    const response = await authService.login(credentials)
    const { token, email, name } = response.data

    localStorage.setItem("token", token)
    localStorage.setItem("user", JSON.stringify({ email, name }))
    setUser({ email, name })

    return response
  }

  const register = async (userData) => {
    const response = await authService.register(userData)
    const { token, email, name } = response.data

    localStorage.setItem("token", token)
    localStorage.setItem("user", JSON.stringify({ email, name }))
    setUser({ email, name })

    return response
  }

  const logout = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("user")
    setUser(null)
  }

  const value = {
    user,
    login,
    register,
    logout,
    loading,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
