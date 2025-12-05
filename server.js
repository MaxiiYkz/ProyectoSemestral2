const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(bodyParser.json());
app.use(cors());

// Configura tu conexión a MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',      // Tu usuario de MySQL
    password: '123456', // Tu contraseña de MySQL
    database: 'proyecto_semestral_db'
});

db.connect(err => {
    if (err) throw err;
    console.log('Conectado a MySQL');
});

// Ruta para Registrar Usuario
app.post('/register', (req, res) => {
    const { email, password } = req.body;
    const sql = 'INSERT INTO users (email, password) VALUES (?, ?)';
    
    db.query(sql, [email, password], (err, result) => {
        if (err.code === 'ER_DUP_ENTRY') {
                res.status(409).send({ message: 'El usuario ya existe' });
        } else {
            res.status(200).send({ message: 'Usuario registrado exitosamente', userId: result.insertId });
        }
    });
});

app.listen(3000, () => {
    console.log('Servidor corriendo en el puerto 3000');
});
app.post('/login', (req, res) => {
    const { email, password } = req.body;
    
    // Consultamos si existe ese usuario con esa contraseña
    const sql = 'SELECT * FROM users WHERE email = ? AND password = ?';
    
    db.query(sql, [email, password], (err, result) => {
        if (err) {
            res.status(500).send({ message: 'Error en el servidor' });
        } else {
            if (result.length > 0) {
                // ¡Login correcto!
                // Devolvemos el ID para que la App sepa quién es
                res.status(200).send({ 
                    message: 'Login exitoso', 
                    userId: result[0].id,
                    email: result[0].email 
                });
            } else {
                // Login incorrecto
                res.status(401).send({ message: 'Credenciales incorrectas' });
            }
        }
    });
});