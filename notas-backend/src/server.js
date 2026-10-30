const express = require('express');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const db = require('./db');
const { firmarToken, verificarToken } = require('./auth');

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 3000;

app.get('/', (req, res) => res.json({ servicio: 'NotasApp API', estado: 'ok' }));

// ---------- AUTH ----------

app.post('/api/auth/register', (req, res) => {
  const { nombre, email, password } = req.body || {};
  if (!nombre || !email || !password) {
    return res.status(400).json({ mensaje: 'nombre, email y password son obligatorios' });
  }
  if (password.length < 6) {
    return res.status(400).json({ mensaje: 'La contrasena debe tener al menos 6 caracteres' });
  }

  const existe = db.prepare('SELECT id FROM usuarios WHERE email = ?').get(email.toLowerCase());
  if (existe) {
    return res.status(409).json({ mensaje: 'El email ya esta registrado' });
  }

  const hash = bcrypt.hashSync(password, 10);
  const info = db
    .prepare('INSERT INTO usuarios (nombre, email, password_hash) VALUES (?, ?, ?)')
    .run(nombre.trim(), email.toLowerCase().trim(), hash);

  res.status(201).json({ id: info.lastInsertRowid, nombre, email: email.toLowerCase() });
});

app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body || {};
  if (!email || !password) {
    return res.status(400).json({ mensaje: 'email y password son obligatorios' });
  }

  const usuario = db
    .prepare('SELECT id, nombre, email, password_hash FROM usuarios WHERE email = ?')
    .get(email.toLowerCase().trim());

  if (!usuario || !bcrypt.compareSync(password, usuario.password_hash)) {
    return res.status(401).json({ mensaje: 'Credenciales incorrectas' });
  }

  const token = firmarToken(usuario);
  res.json({
    token,
    usuario: { id: usuario.id, nombre: usuario.nombre, email: usuario.email }
  });
});

// ---------- NOTAS (protegidas con token) ----------

app.post('/api/notas', verificarToken, (req, res) => {
  const { titulo, contenido } = req.body || {};
  if (!titulo || !titulo.trim() || !contenido || !contenido.trim()) {
    return res.status(400).json({ mensaje: 'titulo y contenido son obligatorios' });
  }

  const info = db
    .prepare('INSERT INTO notas (usuario_id, titulo, contenido) VALUES (?, ?, ?)')
    .run(req.usuario.id, titulo.trim(), contenido.trim());

  const nota = db
    .prepare('SELECT id, titulo, contenido, fecha_creacion FROM notas WHERE id = ?')
    .get(info.lastInsertRowid);

  res.status(201).json(nota);
});

app.get('/api/notas', verificarToken, (req, res) => {
  const notas = db
    .prepare(
      `SELECT id, titulo, contenido, fecha_creacion
       FROM notas WHERE usuario_id = ?
       ORDER BY datetime(fecha_creacion) DESC, id DESC`
    )
    .all(req.usuario.id);

  res.json(notas);
});

app.use((req, res) => res.status(404).json({ mensaje: 'Ruta no encontrada' }));

// 0.0.0.0 para que el emulador (10.0.2.2) o un celular en la misma red puedan conectarse
app.listen(PORT, '0.0.0.0', () => {
  console.log(`NotasApp API escuchando en http://0.0.0.0:${PORT}`);
});