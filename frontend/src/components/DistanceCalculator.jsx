import React, { useState } from "react";
import { LoadScript, DistanceMatrixService } from "@react-google-maps/api";

const DistanceCalculator = () => {
  const [distance, setDistance] = useState("");
  const [duration, setDuration] = useState("");

  const handleCallback = (response) => {
    if (response.rows[0].elements[0].status === "OK") {
      setDistance(response.rows[0].elements[0].distance.text);
      setDuration(response.rows[0].elements[0].duration.text);
    } else {
      setDistance("Distance not available");
      setDuration("Duration not available");
    }
  };

  return (
    <LoadScript googleMapsApiKey="YOUR_API_KEY">
      <DistanceMatrixService
        options={{
          origins: ["New York, NY"],
          destinations: ["Los Angeles, CA"],
          travelMode: "DRIVING",
        }}
        callback={handleCallback}
      />
      {distance && (
        <p>
          Distance: {distance}, Duration: {duration}
        </p>
      )}
    </LoadScript>
  );
};

export default DistanceCalculator;
