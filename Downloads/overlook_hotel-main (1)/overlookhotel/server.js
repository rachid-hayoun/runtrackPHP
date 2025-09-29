const express = require("express");
const cors = require("cors");
const mysql = require("mysql2");

const app = express();
app.use(cors());
app.use(express.json());

const db = mysql.createPool({
  host: "localhost",
  user: "root",
  password: "root",
  database: "overlook"
});

app.post("/api/register", (req, res) => {
  const { nom, prenom, telephone, email, mot_de_passe, role } = req.body;
  if (role !== "client") return res.json({ success: false, message: "Inscription réservée aux clients." });

  db.query("SELECT 1 FROM client WHERE email = ?", [email], (err, results) => {
    if (err) { console.error(err); return res.json({ success: false, message: "Erreur serveur." }); }
    if (results.length > 0) return res.json({ success: false, message: "Email déjà utilisé." });

    db.query(
      "INSERT INTO client (nom, prenom, telephone, email, mot_de_passe, points_fidelite) VALUES (?, ?, ?, ?, ?, 0)",
      [nom, prenom, telephone, email, mot_de_passe],
      (err2) => {
        if (err2) { console.error(err2); return res.json({ success: false, message: "Erreur lors de l'inscription." }); }
        return res.json({ success: true, message: "Inscription réussie !" });
      }
    );
  });
});
app.post("/api/login", (req, res) => {
  const { email, identifiant, mot_de_passe, role } = req.body;

  let table, idField, whereClause, searchValue;
  
  if (role === "client") { 
    table = "client"; 
    idField = "id_client"; 
    whereClause = "email = ?";
    searchValue = email;
  }
  else if (role === "gestionnaire") { 
    table = "gestionnaire"; 
    idField = "id_gestionnaire"; 
    whereClause = "prenom = ?";  
    searchValue = identifiant;
  }
  else {
    return res.json({ success: false, message: "Rôle invalide" });
  }

  db.query(`SELECT ${idField} AS id, nom, prenom, email, mot_de_passe FROM ${table} WHERE ${whereClause}`, [searchValue], (err, results) => {
    if (err) { 
      console.error(err); 
      return res.json({ success: false, message: "Erreur serveur" }); 
    }
    
    if (results.length === 0) {
      return res.json({ success: false, message: "Identifiants incorrects" });
    }

    const user = results[0];
    if (user.mot_de_passe !== mot_de_passe) {
      return res.json({ success: false, message: "Mot de passe incorrect" });
    }

    delete user.mot_de_passe;
    return res.json({ success: true, message: "Connexion réussie", user });
  });
});
app.get("/api/clients", (req, res) => {
  db.query("SELECT id_client, nom, prenom, email FROM client", (err, results) => {
    if (err) { console.error(err); return res.json([]); }
    res.json(results);
  });
});
app.get("/api/employees", (req, res) => {
  db.query("SELECT id_gestionnaire, nom, prenom, email FROM gestionnaire", (err, results) => {
    if (err) { console.error(err); return res.json([]); }
    res.json(results);
  });
});
app.get("/api/chambres", (req, res) => {
  db.query("SELECT * FROM chambre", (err, results) => {
    if (err) { console.error(err); return res.json([]); }
    res.json(results);
  });
});
app.get("/api/reservations", (req, res) => {
  db.query(`
    SELECT r.*, c.nom as client_nom, c.prenom as client_prenom, ch.numero as chambre_numero
    FROM reservation r
    LEFT JOIN client c ON r.id_client = c.id_client
    LEFT JOIN chambre ch ON r.id_chambre = ch.id_chambre
  `, (err, results) => {
    if (err) { console.error(err); return res.json([]); }
    res.json(results);
  });
});

app.post("/api/clients", (req, res) => {
  const { nom, prenom, email, telephone, mot_de_passe } = req.body;
  db.query(
    "INSERT INTO client (nom, prenom, email, telephone, mot_de_passe, points_fidelite) VALUES (?, ?, ?, ?, ?, 0)",
    [nom, prenom, email, telephone, mot_de_passe],
    (err, result) => err ? res.json({success:false}) : res.json({success:true, id: result.insertId})
  );
});

app.put("/api/clients/:id", (req, res) => {
  const { id } = req.params;
  const { nom, prenom, email, telephone, mot_de_passe } = req.body;
  db.query(
    "UPDATE client SET nom=?, prenom=?, email=?, telephone=?, mot_de_passe=? WHERE id_client=?",
    [nom, prenom, email, telephone, mot_de_passe, id],
    (err) => err ? res.json({success:false}) : res.json({success:true})
  );
});

app.delete("/api/clients/:id", (req, res) => {
  const { id } = req.params;
  db.query("DELETE FROM client WHERE id_client=?", [id], (err) => err ? res.json({success:false}) : res.json({success:true}));
});


const PORT = 5000;
app.listen(PORT, () => console.log(`Serveur lancé http://localhost:${PORT}`));