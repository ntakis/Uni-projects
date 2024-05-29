const express = require("express");
const path = require("path");
const uuid = require("uuid");

const app = express();
const port = 8080;
const sessions = {};
const favArrays = {};

app.listen(port);

app.use(express.static("public"));

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

//USERS FOR LOGIN
const users = [
  { username: "user1", password: "pass1" },
  { username: "user2", password: "pass2" },
  { username: "user3", password: "pass3" },
];
console.log("You can use these credentials in category.html");
console.log(users[0]);
console.log(users[1]);
console.log(users[2]);

let uname;

//Login
app.post("/login", (req, res) => {
  const { username, password } = req.body;
  const user = users.find((u) => u.username === username);

  if (user) {
    const sessionId = uuid.v4();
    uname = username;
    sessions[sessionId] = username;
    user.favorites = user.favorites || [];
    res.json({ sessionId, username, user });
  } else {
    res.status(401).json({ error: "Invalid credentials" });
  }
});

//POST method for favorite ads
app.post("/addToFavorites", (req, res) => {
  const { sessionId, adId } = req.body;

  if (!sessions[sessionId] || sessions[sessionId] !== uname) {
    res.status(401).json({ error: "Unauthorized access" });
    return;
  }

  const user = users.find((u) => u.username === uname);

  if (user && user.favorites && user.favorites.includes(adId)) {
    res.status(400).json({ error: "Ad already in favorites" });
    return;
  }

  if (user) {
    user.favorites = user.favorites || [];
    user.favorites.push(adId);
  }

  res.json({ success: true });
});

// API to get favorites adds
app.get("/api/favorite-ads", (req, res) => {
  const { username, sessionId } = req.query;

  if (!sessions[sessionId] || sessions[sessionId] !== username) {
    res.status(401).json({ error: "Unauthorized access" });
    return;
  }

  const user = users.find((u) => u.username === username);
  const userFavArray = user ? user.favorites || [] : [];
  res.json({ favorites: userFavArray });
});
