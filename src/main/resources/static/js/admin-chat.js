let currentChatClientId = null;
let currentChatClientEmail = null;

function switchTab(tab) {
    if (tab === 'main') {
        document.getElementById('content-main').style.display = 'block';
        document.getElementById('content-chat').style.display = 'none';
        if (document.getElementById('menu-main'))
            document.getElementById('menu-main').classList.add('active');
        document.getElementById('menu-chat').classList.remove('active');
    } else {
        document.getElementById('content-main').style.display = 'none';
        document.getElementById('content-chat').style.display = 'block';
        if (document.getElementById('menu-main'))
            document.getElementById('menu-main').classList.remove('active');
        document.getElementById('menu-chat').classList.add('active');
        document.getElementById('admin-chat-badge').style.display = 'none';
        fetchChatUsers();
    }
}

function fetchChatUsers() {
    fetch('ChatAPI?action=getUsers')
        .then(res => res.json())
        .then(users => {
            const list = document.getElementById('chat-user-list');
            list.innerHTML = '';
            if (users.length === 0) {
                list.innerHTML = '<li style="padding:16px; color:#94a3b8; font-size:13px; text-align:center;"><i class="fas fa-inbox"></i><br>Chưa có khách nào nhắn tin</li>';
                return;
            }
            users.forEach(u => {
                let li = document.createElement('li');
                li.className = 'chat-user-item';
                if (u.clientId == currentChatClientId) li.classList.add('active');
                let unreadBadge = (u.unread && u.unread > 0) ? '<span style="background:#d63384;color:white;border-radius:50%;padding:2px 7px;font-size:11px;margin-left:auto;">' + u.unread + '</span>' : '';
                li.innerHTML = '<i class="fas fa-user-circle" style="color:#d63384;font-size:20px;"></i><span style="flex:1;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + escapeHtml(u.email) + '</span>' + unreadBadge;
                li.style.display = 'flex';
                li.style.alignItems = 'center';
                li.style.gap = '8px';
                li.onclick = () => loadChatHistory(u.clientId, u.email);
                list.appendChild(li);
            });
        })
        .catch(e => console.error(e));
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function formatTime(timestampStr) {
    if (!timestampStr) return '';
    var d = new Date(timestampStr.replace(' ', 'T'));
    if (isNaN(d)) return '';
    return d.toLocaleTimeString('vi-VN', {hour: '2-digit', minute: '2-digit'});
}

function loadChatHistory(clientId, email) {
    currentChatClientId = clientId;
    currentChatClientEmail = email;
    document.getElementById('current-chat-name').innerText = email;
    fetchChatUsers();
    const area = document.getElementById('admin-chat-messages');
    area.innerHTML = '<div style="text-align:center;padding:20px;color:#94a3b8;"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';

    fetch('ChatAPI?action=getMessages&clientId=' + clientId + '&viewerRole=admin')
        .then(res => res.json())
        .then(msgs => {
            area.innerHTML = '';
            if (msgs.length === 0) {
                area.innerHTML = '<div style="text-align:center;padding:20px;color:#94a3b8;">Chưa có tin nhắn nào.</div>';
                return;
            }
            msgs.forEach(m => {
                var timeStr = formatTime(m.created_at);
                appendAdminMessage(m.role === 'client' ? 'Khách' : 'Bạn', m.text, m.role, timeStr);
            });
        })
        .catch(e => {
            area.innerHTML = '<div style="text-align:center;padding:20px;color:red;">Lỗi tải tin nhắn.</div>';
        });
}

const adminWsUri = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + (window.contextPath || '') + "/chat-endpoint?clientId=admin&role=admin";
const adminWebsocket = new WebSocket(adminWsUri);

adminWebsocket.onmessage = function (evt) {
    try {
        const data = JSON.parse(evt.data);
        if (data.senderRole === "client") {
            if (document.getElementById('content-chat').style.display !== 'block') {
                document.getElementById('admin-chat-badge').style.display = 'inline-block';
            }
            if (data.clientId == currentChatClientId) {
                var timeStr = new Date().toLocaleTimeString('vi-VN', {hour: '2-digit', minute: '2-digit'});
                appendAdminMessage("Khách", data.msg, "client", timeStr);
            } else {
                fetchChatUsers();
            }
        }
    } catch (e) {}
};

function sendAdminMessage() {
    if (!currentChatClientId) {
        alert("Vui lòng chọn 1 khách hàng bên trái để nhắn tin!");
        return;
    }
    const input = document.getElementById('admin-msg-input');
    const text = input.value.trim();
    if (!text) return;

    var timeStr = new Date().toLocaleTimeString('vi-VN', {hour: '2-digit', minute: '2-digit'});
    appendAdminMessage('Bạn', text, 'admin', timeStr);
    input.value = '';
    input.style.height = '40px';

    const params = new URLSearchParams();
    params.append('clientId', currentChatClientId);
    params.append('clientEmail', currentChatClientEmail);
    params.append('senderRole', 'admin');
    params.append('messageText', text);
    fetch('ChatAPI', {method: 'POST', body: params}).catch(e => console.error(e));

    if (adminWebsocket.readyState === WebSocket.OPEN) {
        adminWebsocket.send(JSON.stringify({
            clientId: currentChatClientId,
            senderRole: "admin",
            msg: text
        }));
    }
}

function appendAdminMessage(name, text, role, timeStr) {
    const container = document.getElementById('admin-chat-messages');
    const msgDiv = document.createElement('div');
    msgDiv.className = "msg-bubble";

    var safeText = escapeHtml(text).replace(/\n/g, '<br>');
    var timeHtml = timeStr ? '<span style="font-size:10px;color:#94a3b8;display:block;margin-top:4px;text-align:right;">' + timeStr + '</span>' : '';

    if (role === "client") {
        msgDiv.style.background = "white";
        msgDiv.style.color = "#2d3436";
        msgDiv.style.border = "1px solid #e2e8f0";
        msgDiv.style.alignSelf = "flex-start";
        msgDiv.style.borderBottomLeftRadius = "0";
        msgDiv.innerHTML = '<b style="color:#0284c7;font-size:12px;"><i class="far fa-user"></i> ' + escapeHtml(name) + '</b><br>' + safeText + timeHtml;
    } else {
        msgDiv.style.background = "#ffeef6";
        msgDiv.style.color = "#d63384";
        msgDiv.style.border = "1px solid #ffd6eb";
        msgDiv.style.alignSelf = "flex-end";
        msgDiv.style.borderBottomRightRadius = "0";
        msgDiv.innerHTML = '<b style="font-size:12px;"><i class="fas fa-headset"></i> ' + escapeHtml(name) + '</b><br>' + safeText + timeHtml;
    }

    container.appendChild(msgDiv);
    container.scrollTop = container.scrollHeight;
}

window.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('admin-msg-input');
    if (input) {
        input.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendAdminMessage();
            }
        });
        // Auto-resize textarea nếu admin-msg-input là textarea
        input.addEventListener('input', function() {
            this.style.height = '40px';
            this.style.height = (this.scrollHeight) + 'px';
        });
    }
    // Refresh danh sách khách mỗi 15 giây
    setInterval(function() {
        if (document.getElementById('content-chat') &&
            document.getElementById('content-chat').style.display === 'block') {
            fetchChatUsers();
        }
    }, 15000);
});
