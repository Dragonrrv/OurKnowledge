package com.example.ourknowledgebackend.service;

import java.util.List;
import java.util.Objects;

public class Block<T> {
	
	private final List<T> items;
    private final boolean existMoreItems;

	private final int page;

	private final int size;

    public Block(List<T> items, boolean existMoreItems, int page, int size) {
        
        this.items = items;
        this.existMoreItems = existMoreItems;
		this.page = page;
		this.size = size;
	}
    
    public List<T> getItems() {
        return items;
    }
    
    public boolean getExistMoreItems() {
        return existMoreItems;
    }

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Block<?> block = (Block<?>) o;
		return existMoreItems == block.existMoreItems && page == block.page && size == block.size && items.equals(block.items);
	}

	@Override
	public int hashCode() {
		return Objects.hash(items, existMoreItems, page, size);
	}
}
