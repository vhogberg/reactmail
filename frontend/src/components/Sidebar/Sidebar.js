import React from 'react';
import "./Sidebar.css";

const folderMappings = {
  Inbox: "INBOX",
  Sent: "[Gmail]/Sent Mail",
  Drafts: "[Gmail]/Drafts",
  Trash: "[Gmail]/Trash",
  Spam: "[Gmail]/Spam",
  Starred: "[Gmail]/Starred",
  Important: "[Gmail]/Important",
  AllMail: "[Gmail]/All Mail",
};


function Sidebar({ onComposeClick, onRefreshClick, onSelectFolder, selectedFolder }) {
  return (
    <div className="sidebar">
      <h2>React<span id="logo-span">Mail</span></h2>
      <div className="sidebar-menu">
        {Object.keys(folderMappings).map((folder) => (
          <div
            key={folder}
            className={`sidebar-item ${selectedFolder === folderMappings[folder] ? "sidebar-item-active" : ""}`}
            onClick={() => onSelectFolder(folderMappings[folder])} // Set correct folder format (gmail)
          >
            {folder}
          </div>
        ))}
      </div>
      <button className="compose-btn" onClick={onComposeClick}>Compose</button>
      <button className="refresh-btn" onClick={onRefreshClick}>Refresh</button>
    </div>
  );
}

export default Sidebar;