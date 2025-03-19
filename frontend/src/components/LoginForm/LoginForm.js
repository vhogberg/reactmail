import React, { useState } from 'react';
import "./LoginForm.css";

// Login form component

function LoginForm({ onLogin }) {
    const [formData, setFormData] = useState({
        // prefill server details
        imapHost: 'imap.gmail.com',
        imapPort: '993',
        smtpHost: 'smtp.gmail.com',
        smtpPort: '465',
        username: '',
        password: ''
    });

    const [showAdvanced, setShowAdvanced] = useState(false);

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

    // only show advanced fields if user clicks it
    const toggleAdvanced = () => {
        setShowAdvanced(!showAdvanced);
    };

    return (
        <div className="login-container">
            <div className="login-form">
                <h2>React<span id="logo-span">Mail</span></h2>
                <form onSubmit={handleSubmit}>
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

                    <div className="advanced-settings">
                        <button
                            type="button"
                            className="advanced-toggle"
                            onClick={toggleAdvanced}
                        >
                            <span className="cog-icon">⚙️</span>
                            {showAdvanced ? "Hide Advanced Settings" : "Advanced Settings"}
                        </button>

                        {showAdvanced && (
                            <div className="advanced-fields">
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
                            </div>
                        )}
                    </div>
                    <button type="submit" className="login-btn">Login</button>
                </form>
            </div>
        </div>
    );
}

export default LoginForm;