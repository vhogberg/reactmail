
// components/EmailView.js
import React from 'react';
import "./EmailView.css";

// Email view, i.e one selected email

function EmailView({ email, onClose }) {

    // handle attachment download
    const handleDownload = (attachment) => {
        // get via api/download-attachment and its id
        const downloadUrl = `http://localhost:8080/api/download-attachment/${attachment.id}`;

        // create a temporary anchor element
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = attachment.name;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    return (
        <div className="email-view">
            <div className="email-view-header">
                <h3>{email.subject}</h3>
                <button className="close-btn" onClick={onClose}>×</button>
            </div>
            <div className="email-view-meta">
                <div className="email-view-sender">
                    <strong>From:</strong> {email.from}
                </div>
                <div className="email-view-date">
                    <span>{email.date}</span>
                </div>
            </div>
            <div className="email-view-body">
                {email.body}
            </div>
            {email.attachments && email.attachments.length > 0 && (
                <div className="email-view-attachments">
                    <h4>Attachments:</h4>
                    <div className="attachment-list">
                        {email.attachments.map((attachment, index) => (
                            <div key={index} className="attachment-item">
                                <span>{attachment.name}</span>
                                <button
                                    className="download-btn"
                                    onClick={() => handleDownload(attachment)}
                                >
                                    Download
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}

export default EmailView;