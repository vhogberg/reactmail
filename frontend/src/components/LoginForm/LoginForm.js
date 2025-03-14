// components/LoginForm.js
import React, { useState } from 'react';
import "./LoginForm.css";

// Login page with 6 fields

function LoginForm({ onLogin }) {
    const [formData, setFormData] = useState({
        // prefill first 4
        imapHost: 'imap.gmail.com',
        imapPort: '993',
        smtpHost: 'smtp.gmail.com',
        smtpPort: '465',
        username: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onLogin(formData);
    };

    return (
        <div className="login-container">
            <div className="login-form">
                <h2>ReactMail Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>IMAP Server:</label>
                        <input
                            type="text"
                            name="imapHost"
                            value={formData.imapHost}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>IMAP Port:</label>
                        <input
                            type="text"
                            name="imapPort"
                            value={formData.imapPort}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>SMTP Server:</label>
                        <input
                            type="text"
                            name="smtpHost"
                            value={formData.smtpHost}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>SMTP Port:</label>
                        <input
                            type="text"
                            name="smtpPort"
                            value={formData.smtpPort}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Email Address:</label>
                        <input
                            type="email"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Password:</label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <button type="submit" className="login-btn">Login</button>
                </form>
            </div>
        </div>
    );
}

export default LoginForm;