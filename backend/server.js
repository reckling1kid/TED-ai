import express from "express";
import { GoogleGenAI } from "@google/genai";

const app = express();

app.disable("x-powered-by");
app.use(express.json({ limit: "20kb" }));

const PORT = process.env.PORT || 8080;
const apiKey = process.env.GEMINI_API_KEY;
const ai = apiKey ? new GoogleGenAI({ apiKey }) : null;

// Health check: confirms the backend process is running.
app.get("/", (_req, res) => {
  res.json({
    name: "Kokoro",
    status: "running",
    aiConfigured: Boolean(ai)
  });
});

// Chat endpoint: sends a message to Gemini.
app.post("/chat", async (req, res) => {
  if (!ai) {
    return res.status(503).json({
      error: "Kokoro AI is not configured yet."
    });
  }

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

  try {
    const response = await ai.models.generateContent({
      model: "gemini-3.8-flash",
      contents: message.trim(),
      config: {
        tools: [{ googleSearch: {} }]
      }
    });

    return res.json({
      reply: response.text ||
        "Sorry, I couldn't generate a response."
    });
  } catch (error) {
    console.error("Kokoro AI request failed:", error.message);

    return res.status(502).json({
      error: "Kokoro couldn't get an answer. Please try again."
    });
  }
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`Kokoro backend listening on port ${PORT}`);
});
