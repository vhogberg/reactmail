// App.js
import React, { useEffect, useState } from 'react';
import './App.css';
import ComposeEmail from './components/ComposeEmail/ComposeEmail';
import EmailList from './components/EmailList/EmailList';
import EmailView from './components/EmailView/EmailView';
import LoginForm from './components/LoginForm/LoginForm';
import Sidebar from './components/Sidebar/Sidebar';

function App() {
  const [emails, setEmails] = useState([]);
  const [selectedEmail, setSelectedEmail] = useState(null);
  const [showCompose, setShowCompose] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [credentials, setCredentials] = useState({
    imapHost: '',
    imapPort: '',
    smtpHost: '',
    smtpPort: '',
    username: '',
    password: ''
  });
  const [isEmailListHidden, setIsEmailListHidden] = useState(false);

  // Show the clicked email
  const handleEmailSelect = (email) => {
    setSelectedEmail(email);
    setShowCompose(false);
    setIsEmailListHidden(true); // Hide email list when an email is selected
  };

  // Load emails when user logged in
  useEffect(() => {
    if (isLoggedIn) {
      fetchEmails();
    }
  }, [isLoggedIn]);

  // method that fetches the emails from api
  const fetchEmails = async () => {
    try {
      // localhost currently
      const response = await fetch('http://localhost:8080/api/receive-emails', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          host: credentials.imapHost,
          port: credentials.imapPort,
          username: credentials.username,
          password: credentials.password
        }),
      });

      if (response.ok) {
        const data = await response.json();
        setEmails(data);
      } else {
        console.error('Failed to fetch emails');
      }
    } catch (error) {
      console.error('Error fetching emails:', error);
    }
  };

  const handleLogin = (loginData) => {
    setCredentials(loginData);
    setIsLoggedIn(true);
  };

  const handleSendEmail = async (emailData) => {
    try {
      // object for sending both text fields and files
      const formData = new FormData();
      
      // Add text fields
      formData.append('host', credentials.smtpHost);
      formData.append('port', credentials.smtpPort);
      formData.append('username', credentials.username);
      formData.append('password', credentials.password);
      formData.append('to', emailData.to);
      formData.append('subject', emailData.subject);
      formData.append('body', emailData.body);
      
      // Add attachments if there are any
      if (emailData.attachments && emailData.attachments.length > 0) {
        emailData.attachments.forEach(file => {
          formData.append('attachments', file);
        });
      }

      const response = await fetch('http://localhost:8080/api/send-email', {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        setShowCompose(false);
        // Refresh inbox after sending
        fetchEmails();
      } else {
        const errorData = await response.json();
        console.error('Failed to send email:', errorData.message);
        alert(`Failed to send email: ${errorData.message}`);
      }
    } catch (error) {
      console.error('Error sending email:', error);
      alert('Error sending email. Please try again.');
    }
  };

  if (!isLoggedIn) {
    return <LoginForm onLogin={handleLogin} />;
  }

  return (
    <div className="app">
      <div className="app-container">
        <Sidebar onComposeClick={() => setShowCompose(true)} onRefreshClick={fetchEmails} />

        <div className="main-content">
          <EmailList
            emails={emails}
            onSelectEmail={handleEmailSelect}
            selectedEmailId={selectedEmail ? selectedEmail.id : null}
            hidden={isEmailListHidden}
          />

          {selectedEmail && (
            <EmailView
              email={selectedEmail}
              onClose={() => {
                setSelectedEmail(null);
                setIsEmailListHidden(false); // Show email list when closing an email
              }}
            />
          )}
        </div>
      </div>

      {showCompose && (
        <ComposeEmail
          onClose={() => setShowCompose(false)}
          onSend={handleSendEmail}
        />
      )}
    </div>
  );
}

export default App;