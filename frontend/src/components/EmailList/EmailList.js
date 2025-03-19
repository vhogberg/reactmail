// components/EmailList.js
import { default as React, useEffect, useState } from 'react';
import "./EmailList.css";

// Reverse mapping function to convert "[Gmail]/Trash" to "Trash" etc.
const getDisplayFolderName = (selectedFolder) => {
    const reverseMapping = {
        "INBOX": "Inbox",
        "[Gmail]/Sent Mail": "Sent",
        "[Gmail]/Drafts": "Drafts",
        "[Gmail]/Trash": "Trash",
        "[Gmail]/Spam": "Spam",
        "[Gmail]/Starred": "Starred",
        "[Gmail]/Important": "Important",
        "[Gmail]/All Mail": "All Mail"
    };
    return reverseMapping[selectedFolder] || "Inbox"; // Default to inbox
};

// component with a list of most recent emails
function EmailList({ emails, onSelectEmail, selectedEmailId, hidden, selectedFolder }) {

    // Dark mode handling
    const [darkMode, setDarkMode] = useState(
        window.matchMedia('(prefers-color-scheme: dark)').matches
    );
    useEffect(() => {
        document.documentElement.setAttribute('data-theme', darkMode ? 'dark' : 'light');
    }, [darkMode]);

    if (hidden) return null;

    return (
        <div className="email-list">
            <div className="email-list-header">
            <h2>{getDisplayFolderName(selectedFolder)}</h2> {/* Dynamically set h2 title depending on inbox/trash etc */}
                <button
                    onClick={() => setDarkMode(!darkMode)}
                    className="theme-toggle"
                    aria-label={darkMode ? 'Switch to light mode' : 'Switch to dark mode'}
                >
                    {darkMode ? '☀️' : '🌙'}
                </button>
            </div>
            <div className="email-list-items">
                {(() => {
                    // if there are no emails just show text "No emails found"
                    if (emails.length === 0) {
                        return <div className="no-emails">No emails found</div>;
                    }

                    // otherwise return list and handle clicks on specific email
                    return emails.map((email) => (
                        <div
                            key={email.id}
                            className={`email-item ${selectedEmailId === email.id ? 'email-item-selected' : ''}`}
                            onClick={() => onSelectEmail(email)}
                        >
                            <div className="email-sender">{email.from}</div>
                            <div className="email-subject">{email.subject}</div>
                            <div className="email-date">{email.date}</div>
                        </div>
                    ));
                })()}
            </div>

        </div>
    );
}


export default EmailList;