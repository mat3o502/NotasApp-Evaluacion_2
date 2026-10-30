const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'cambia-este-secreto-en-produccion';
const JWT_EXPIRES = '12h';

function firmarToken(usuario) {
  return jwt.sign(
    { id: usuario.id, email: usuario.email, nombre: usuario.nombre },
    JWT_SECRET,
    { expiresIn: JWT_EXPIRES }
  );
}

// Middleware: valida el header "Authorization: Bearer <token>"
function verificarToken(req, res, next) {
  const header = req.headers['authorization'] || '';
  const [tipo, token] = header.split(' ');

  if (tipo !== 'Bearer' || !token) {
    return res.status(401).json({ mensaje: 'Token no proporcionado' });
  }

  try {
    req.usuario = jwt.verify(token, JWT_SECRET);
    next();
  } catch (e) {
    return res.status(401).json({ mensaje: 'Token invalido o expirado' });
  }
}

module.exports = { firmarToken, verificarToken };