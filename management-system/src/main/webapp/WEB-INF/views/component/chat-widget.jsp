<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Chat Widget Styles -->
<style>
    .chat-widget {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 1000;
    }
    
    .chat-button {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #06b6d4, #8b5cf6);
        border: none;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: transform 0.3s ease;
        color: white;
    }
    
    .chat-button:hover {
        transform: scale(1.1);
    }
    
    .chat-button i {
        font-size: 24px;
    }
    
    .chat-window {
        display: none;
        position: fixed;
        bottom: 90px;
        right: 20px;
        width: 380px;
        height: 500px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
        flex-direction: column;
        overflow: hidden;
    }
    
    .chat-window.active {
        display: flex;
    }
    
    .chat-header {
        background: linear-gradient(135deg, #06b6d4, #8b5cf6);
        color: white;
        padding: 16px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    
    .chat-header h5 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
    }
    
    .chat-close {
        background: transparent;
        border: none;
        color: white;
        font-size: 24px;
        cursor: pointer;
        padding: 0;
        line-height: 1;
    }
    
    .chat-messages {
        flex: 1;
        overflow-y: auto;
        padding: 16px;
        background: #f8fafc;
    }
    
    .chat-message {
        margin-bottom: 12px;
        display: flex;
        flex-direction: column;
    }
    
    .message-bubble {
        padding: 10px 14px;
        border-radius: 12px;
        max-width: 80%;
        word-wrap: break-word;
        white-space: pre-wrap;
        font-size: 14px;
        line-height: 1.5;
    }
    
    .user-message .message-bubble {
        background: linear-gradient(135deg, #06b6d4, #8b5cf6);
        color: white;
        align-self: flex-end;
        margin-left: auto;
    }
    
    .bot-message .message-bubble {
        background: white;
        color: #0f172a;
        border: 1px solid #e2e8f0;
        align-self: flex-start;
    }
    
    .message-time {
        font-size: 11px;
        color: #64748b;
        margin-top: 4px;
    }
    
    .user-message .message-time {
        text-align: right;
        align-self: flex-end;
    }
    
    .bot-message .message-time {
        text-align: left;
        align-self: flex-start;
    }
    
    .chat-input-container {
        padding: 16px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        gap: 8px;
    }
    
    .chat-input {
        flex: 1;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        padding: 10px 16px;
        font-size: 14px;
        outline: none;
    }
    
    .chat-input:focus {
        border-color: #06b6d4;
    }
    
    .chat-send-btn {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(135deg, #06b6d4, #8b5cf6);
        border: none;
        color: white;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    
    .chat-send-btn:hover {
        opacity: 0.9;
    }
    
    .chat-send-btn:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
    
    .typing-indicator {
        display: none;
        padding: 10px 14px;
        background: white;
        border: 1px solid #e2e8f0;
        border-radius: 12px;
        max-width: 60px;
    }
    
    .typing-indicator.active {
        display: flex;
        align-items: center;
        gap: 4px;
    }
    
    .typing-dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #64748b;
        animation: typing 1.4s infinite;
    }
    
    .typing-dot:nth-child(2) {
        animation-delay: 0.2s;
    }
    
    .typing-dot:nth-child(3) {
        animation-delay: 0.4s;
    }
    
    @keyframes typing {
        0%, 60%, 100% { transform: translateY(0); }
        30% { transform: translateY(-10px); }
    }
    
    /* Notification badge */
    .chat-badge {
        position: absolute;
        top: -5px;
        right: -5px;
        background: #ef4444;
        color: white;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        font-size: 11px;
        display: none;
        align-items: center;
        justify-content: center;
        font-weight: bold;
    }
    
    .chat-badge.active {
        display: flex;
    }
</style>

<!-- Chat Widget HTML -->
<div class="chat-widget">
    <!-- Chat Button -->
    <button class="chat-button" id="chatToggle">
        <i class="fas fa-robot"></i>
        <span class="chat-badge" id="chatBadge">1</span>
    </button>
    
    <!-- Chat Window -->
    <div class="chat-window" id="chatWindow">
        <!-- Header -->
        <div class="chat-header">
            <div>
                <h5><i class="fas fa-robot me-2"></i>Package Assistant</h5>
                <small style="opacity: 0.9;">Powered by AI</small>
            </div>
            <button class="chat-close" id="chatClose">×</button>
        </div>
        
        <!-- Messages Area -->
        <div class="chat-messages" id="chatMessages">
            <div class="chat-message bot-message">
                <div class="message-bubble">
                    👋 Hi! I'm your AI package assistant. I can help you:
                    
- Check package status
- Find pickup hours
- Track deliveries
- Answer questions

What would you like to know?
                </div>
                <span class="message-time">Just now</span>
            </div>
            
            <!-- Typing indicator -->
            <div class="typing-indicator" id="typingIndicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        </div>
        
        <!-- Input Area -->
        <div class="chat-input-container">
            <input type="text" 
                   class="chat-input" 
                   id="chatInput" 
                   placeholder="Ask about your packages..."
                   autocomplete="off">
            <button class="chat-send-btn" id="chatSend">
                <i class="fas fa-paper-plane"></i>
            </button>
        </div>
    </div>
</div>

<!-- Chat Widget Script -->
<script>
$(document).ready(function() {
    var chatWindow = $('#chatWindow');
    var chatMessages = $('#chatMessages');
    var chatInput = $('#chatInput');
    var chatSend = $('#chatSend');
    var typingIndicator = $('#typingIndicator');
    var chatBadge = $('#chatBadge');
    
    // Show welcome badge
    chatBadge.addClass('active');
    
    // Toggle chat window
    $('#chatToggle').click(function() {
        chatWindow.toggleClass('active');
        chatBadge.removeClass('active'); // Hide badge when opened
        
        if (chatWindow.hasClass('active')) {
            chatInput.focus();
        }
    });
    
    // Close chat
    $('#chatClose').click(function() {
        chatWindow.removeClass('active');
    });
    
    // Send message on button click
    chatSend.click(function() {
        sendMessage();
    });
    
    // Send message on Enter key
    chatInput.keypress(function(e) {
        if (e.which === 13 && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });
    
    function sendMessage() {
        var message = chatInput.val().trim();
        
        if (message === '') {
            return;
        }
        
        // Add user message to chat
        addMessage(message, 'user');
        
        // Clear input
        chatInput.val('');
        
        // Disable input while processing
        chatInput.prop('disabled', true);
        chatSend.prop('disabled', true);
        
        // Show typing indicator
        typingIndicator.addClass('active');
        scrollToBottom();
        
        // Send to server
        $.ajax({
            url: '${pageContext.request.contextPath}/chat/message',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ message: message }),
            success: function(response) {
                if (response.success) {
                    // Small delay to make it feel more natural
                    setTimeout(function() {
                        addMessage(response.response, 'bot');
                    }, 500);
                } else {
                    addMessage('Sorry, I encountered an error. Please try again.', 'bot');
                }
            },
            error: function(xhr) {
                var errorMsg = 'Sorry, I\'m having trouble right now. Please try again or contact the front desk.';
                addMessage(errorMsg, 'bot');
            },
            complete: function() {
                // Hide typing indicator after response
                setTimeout(function() {
                    typingIndicator.removeClass('active');
                    
                    // Re-enable input
                    chatInput.prop('disabled', false);
                    chatSend.prop('disabled', false);
                    chatInput.focus();
                }, 600);
            }
        });
    }
    
    function addMessage(text, type) {
        var messageClass = type === 'user' ? 'user-message' : 'bot-message';
        var time = new Date().toLocaleTimeString('en-US', { 
            hour: 'numeric', 
            minute: '2-digit' 
        });
        
        var messageHtml = 
            '<div class="chat-message ' + messageClass + '">' +
                '<div class="message-bubble">' + escapeHtml(text) + '</div>' +
                '<span class="message-time">' + time + '</span>' +
            '</div>';
        
        // Insert before typing indicator
        typingIndicator.before(messageHtml);
        scrollToBottom();
    }
    
    function scrollToBottom() {
        chatMessages.scrollTop(chatMessages[0].scrollHeight);
    }
    
    function escapeHtml(text) {
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
});
</script>