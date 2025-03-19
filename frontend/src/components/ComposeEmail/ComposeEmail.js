import React, { useState } from 'react';
import "./ComposeEmail.css";

// Popup for when pressing compose, i.e writing an email

function ComposeEmail({ onClose, onSend }) {
    const [to, setTo] = useState('');
    const [subject, setSubject] = useState('');
    const [body, setBody] = useState('');
    const [attachments, setAttachments] = useState([]);

    const [showSent, setShowSent] = useState(false); // State for sent message

    const handleSubmit = (e) => {
        e.preventDefault();
        onSend({
            to,
            subject,
            body,
            attachments
        });
        // Show "Sent!" confirmation
        setShowSent(true);
        setTimeout(() => setShowSent(false), 2000); // Hide after 2 sec
    };

    const handleFileChange = (e) => {
        setAttachments([...attachments, ...e.target.files]);
    };

    const removeAttachment = (index) => {
        const newAttachments = [...attachments];
        newAttachments.splice(index, 1);
        setAttachments(newAttachments);
    };

    return (
        <div className="compose-overlay">
            <div className="compose-container">
                <div className="compose-header">
                    <h3>New Message</h3>
                    <button className="close-button" onClick={onClose}>×</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="compose-field">
                        <label>To:</label>
                        <input
                            type="email"
                            value={to}
                            onChange={(e) => setTo(e.target.value)}
                            required
                        />
                    </div>
                    <div className="compose-field">
                        <label>Subject:</label>
                        <input
                            type="text"
                            value={subject}
                            onChange={(e) => setSubject(e.target.value)}
                        />
                    </div>
                    <div className="compose-body">
                        <textarea
                            value={body}
                            onChange={(e) => setBody(e.target.value)}
                            required
                        />
                    </div>

                    <div className="compose-attachments">
                        {attachments.length > 0 && (
                            <div className="attachment-list">
                                <h4>Attachments:</h4>
                                <ul>
                                    {attachments.map((file, index) => (
                                        <li key={index}>
                                            {file.name} ({(file.size / 1024).toFixed(2)} KB)
                                            <button
                                                type="button"
                                                onClick={() => removeAttachment(index)}
                                                className="remove-attachment"
                                            >
                                                ×
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        <div className="file-upload">
                            <label htmlFor="file-input" className="file-label">
                                Attach Files
                            </label>
                            <input
                                id="file-input"
                                type="file"
                                multiple
                                onChange={handleFileChange}
                                className="file-input"
                            />
                        </div>
                    </div>

                    <div className="compose-actions">
                        <button type="submit" className="send-button">Send</button>
                    </div>
                </form>
                {showSent && <div className="sent-confirmation">Sent!</div>}
            </div>
        </div>
    );
}

export default ComposeEmail;