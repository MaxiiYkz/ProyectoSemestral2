const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(bodyParser.json());
app.use(cors());


const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',      
    password: '123456', 
    database: 'proyecto_semestral_db'
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a MySQL:', err);
    } else {
        console.log('Conectado a MySQL');
    }
});

app.post('/register', (req, res) => {
    const { email, password } = req.body;
    

    if (!email || !password) {
        return res.status(400).send({ message: 'Faltan datos' });
    }

    const sql = 'INSERT INTO users (email, password) VALUES (?, ?)';
    
    db.query(sql, [email, password], (err, result) => {
        if (err) {
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(409).send({ message: 'El usuario ya existe' });
            }
            console.log("Error SQL:", err);
            return res.status(500).send({ message: 'Error al registrar' });
        } 
        

        res.status(200).send({ message: 'Usuario registrado exitosamente', userId: result.insertId });
    });
});

app.post('/login', (req, res) => {
    const { email, password } = req.body;
    
    const sql = 'SELECT * FROM users WHERE email = ? AND password = ?';
    
    db.query(sql, [email, password], (err, result) => {
        if (err) {
            console.log("Error en Login:", err);
            res.status(500).send({ message: 'Error en el servidor' });
        } else {
            if (result.length > 0) {
                res.status(200).send({ 
                    message: 'Login exitoso', 
                    userId: result[0].id,
                    email: result[0].email 
                });
            } else {
                res.status(401).send({ message: 'Credenciales incorrectas' });
            }
        }
    });
});

app.listen(3000, () => {
    console.log('Servidor corriendo en el puerto 3000');
});