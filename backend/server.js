import express from "express";
import { GoogleGenAI } from "@google/genai";

const app = express();
app.use(express.json({ limit: "20kb" }));

const apiKey = process.env.GEMINI_API_KEY;

if (!apiKey) {
  throw new Error("GEMINI_API_KEY environment variable is required");
}

const ai = new GoogleGenAI({ apiKey });
const PORT = process.env.PORT || 8080;

app.get("/", (_req, res) => {
  res.json({
    name: "Kokoro",
    status: "running"
  });
});

app.post("/chat", async (req, res) => {
  try {
    const message = req.body?.message;

    if (typeof message !== "string" || !message.trim()) {
      return res.status(400).json({
        error: "Please enter a message."
      });
    }

    if (message.length > 8000) {
      return res.status(413).json({
        error: "Message is too long."
      });
    }

    const response = await ai.models.generateContent({
      model: "gemini-3.6-flash",
      contents: message.trim(),
      config: {
        tools: [{ googleSearch: {} }]
      }
    });

    res.json({
      reply: response.text || "Sorry, I couldn't generate a response."
    });
  } catch (error) {
    console.error("Kokoro AI request failed:", error.message);

    res.status(502).json({
      error: "Kokoro couldn't get an answer. Please try again."
    });
  }
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`Kokoro backend listening on port ${PORT}`);
});
