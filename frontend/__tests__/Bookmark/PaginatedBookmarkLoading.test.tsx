import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { BookmarkProvider } from '@/contexts/BookmarkContext';
import BookmarkCardsView from '@/components/CardView/BookmarkCardsView';
import api from '@/api/Api';
// We'll use our mock contexts instead of the real ones
import { vi } from 'vitest';
import { createContext } from 'react';

// Create a mock tag context
export const MockTagContext = createContext<{tagMap: Map<number, any>}>({
  tagMap: new Map()
});

// Create a mock selected context
export const MockSelectedContext = createContext<{
  selected: string[],
  setSelected: (selected: string[]) => void
}>({
  selected: [],
  setSelected: () => {}
});

// Mock the API
vi.mock('@/api/Api', () => ({
  default: {
    getPaginatedBookmarks: vi.fn(),
  },
}));

// Setup mock UseAuth
vi.mock('@components/UseAuth', () => ({
  default: () => true,
}));

describe('Paginated Bookmark Loading', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    
    // Mock first page response
    api.getPaginatedBookmarks.mockResolvedValueOnce({
      data: {
        bookmarks: [
          { id: 1, title: 'Bookmark 1', url: 'https://example.com/1' },
          { id: 2, title: 'Bookmark 2', url: 'https://example.com/2' },
        ],
        totalPages: 2,
        currentPage: 1
      }
    });
    
    // Mock second page response
    api.getPaginatedBookmarks.mockResolvedValueOnce({
      data: {
        bookmarks: [
          { id: 3, title: 'Bookmark 3', url: 'https://example.com/3' },
          { id: 4, title: 'Bookmark 4', url: 'https://example.com/4' },
        ],
        totalPages: 2,
        currentPage: 2
      }
    });
  });

  test('should load the first page of bookmarks on initial render', async () => {
    // Arrange
    const mockTagsMap = new Map();
    
    render(
      <MockTagContext.Provider value={{ tagMap: mockTagsMap }}>
        <MockSelectedContext.Provider value={{ selected: [], setSelected: vi.fn() }}>
          <BookmarkProvider>
            <BookmarkCardsView />
          </BookmarkProvider>
        </MockSelectedContext.Provider>
      </MockTagContext.Provider>
    );

    // Assert
    expect(api.getPaginatedBookmarks).toHaveBeenCalledWith(1, 10);
    
    // Wait for bookmarks to load (with a generous timeout)
    await waitFor(() => {
      expect(api.getPaginatedBookmarks).toHaveBeenCalled();
    }, { timeout: 3000 });
  });

  test('should automatically load the next page in test environment', async () => {
    // Arrange
    const mockTagsMap = new Map();
    
    render(
      <MockTagContext.Provider value={{ tagMap: mockTagsMap }}>
        <MockSelectedContext.Provider value={{ selected: [], setSelected: vi.fn() }}>
          <BookmarkProvider>
            <BookmarkCardsView />
          </BookmarkProvider>
        </MockSelectedContext.Provider>
      </MockTagContext.Provider>
    );

    // Assert that both API calls were made due to our test environment logic
    await waitFor(() => {
      expect(api.getPaginatedBookmarks).toHaveBeenCalledTimes(2);
      expect(api.getPaginatedBookmarks).toHaveBeenCalledWith(1, 10);
      expect(api.getPaginatedBookmarks).toHaveBeenCalledWith(2, 10);
    }, { timeout: 3000 });
  });
});