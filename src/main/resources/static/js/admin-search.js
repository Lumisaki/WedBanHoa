function filterAdminTable(input) {
    const filter = input.value.toLowerCase().trim();
    
    // Đảm bảo wrapper của input có position relative để chứa hộp gợi ý
    const inputParent = input.parentElement;
    if (window.getComputedStyle(inputParent).position === 'static') {
        inputParent.style.position = 'relative';
    }

    // Tìm hoặc tạo hộp gợi ý
    let suggBox = inputParent.querySelector('.suggestions-box');
    if (!suggBox) {
        suggBox = document.createElement('div');
        suggBox.className = 'suggestions-box';
        inputParent.appendChild(suggBox);
        
        // Ẩn hộp gợi ý khi click ra ngoài
        document.addEventListener('click', function(e) {
            if (!inputParent.contains(e.target)) {
                suggBox.style.display = 'none';
            }
        });
    }

    // Nếu không nhập gì thì ẩn hộp gợi ý và hiện tất cả
    if (!filter) {
        suggBox.style.display = 'none';
        suggBox.innerHTML = '';
    } else {
        suggBox.style.display = 'block';
        suggBox.innerHTML = ''; // Clear cũ
    }

    // Tìm bảng để lọc
    let wrapper = input.closest('.table-card') || input.closest('.main-content');
    if (!wrapper) wrapper = document;

    const tables = wrapper.querySelectorAll('table');
    let suggestionCount = 0;
    const addedSuggestions = new Set();

    tables.forEach(table => {
        const tbody = table.querySelector('tbody');
        if (!tbody) return;
        
        const rows = tbody.getElementsByTagName('tr');
        for (let i = 0; i < rows.length; i++) {
            const rowText = rows[i].textContent || rows[i].innerText;
            
            if (rowText.toLowerCase().indexOf(filter) > -1) {
                rows[i].style.display = "";
                
                // Trích xuất từ khóa gợi ý nếu đang gõ
                if (filter && suggestionCount < 6) {
                    const cells = rows[i].getElementsByTagName('td');
                    for (let j = 0; j < cells.length; j++) {
                        let cellText = (cells[j].textContent || cells[j].innerText).trim();
                        // Dọn dẹp khoảng trắng thừa
                        cellText = cellText.replace(/\s+/g, ' ');
                        
                        if (cellText.toLowerCase().indexOf(filter) > -1 && cellText.length > 0) {
                            // Chỉ lấy đoạn text vừa phải (tối đa 40 ký tự)
                            if (cellText.length > 40) {
                                cellText = cellText.substring(0, 40) + '...';
                            }
                            
                            if (!addedSuggestions.has(cellText.toLowerCase())) {
                                addedSuggestions.add(cellText.toLowerCase());
                                suggestionCount++;
                                
                                const item = document.createElement('div');
                                item.className = 'suggestion-item';
                                item.innerHTML = '<i class="fas fa-search"></i> ' + cellText;
                                
                                // Khi click vào gợi ý -> tự động điền vào ô tìm kiếm và lọc lại
                                item.onclick = function() {
                                    input.value = cellText.replace('...', '');
                                    suggBox.style.display = 'none';
                                    filterAdminTable(input); // Gọi lại để lọc bảng theo từ này
                                };
                                
                                suggBox.appendChild(item);
                            }
                            break; // Lấy cell đầu tiên khớp thôi
                        }
                    }
                }
            } else {
                rows[i].style.display = "none";
            }
        }
    });

    if (filter && suggestionCount === 0) {
        suggBox.innerHTML = '<div class="suggestion-item" style="color: #94a3b8; font-style: italic;">Không tìm thấy kết quả...</div>';
    }
}
