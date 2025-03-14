import React from 'react';
import "./Sidebar.css";

// Sidebar containing logo, folders, compose button and refresh
function Sidebar({ onComposeClick, onRefreshClick }) {
  return (
    <div className="sidebar">
      <h2>React<span id="logo-span">Mail</span></h2>
      <div className="sidebar-menu">
        <div className="sidebar-item sidebar-item-active">Inbox</div>
        
        {/*TODO maybe implement? <div className="sidebar-item">Sent</div>
        <div className="sidebar-item">Trash</div> */}
      </div>
      <button className="compose-btn" onClick={onComposeClick}>
        Compose
      </button>
      <button className="refresh-btn" onClick={onRefreshClick}>
        Refresh
      </button>
    </div>
  );
}

export default Sidebar;