// components/EmailList.js
import React from 'react';
import "./EmailList.css";

// component with a list of most recent emails
function EmailList({ emails, onSelectEmail, selectedEmailId, hidden }) {

    if (hidden) return null;

    return (
        <div className="email-list">
            <div className="email-list-header">
                <h2>Inbox</h2>
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