package telran.album.test;

import telran.album.dao.Album;
import telran.album.dao.AlbumImpl;
import telran.album.model.Photo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final int capacity = 6;
    private final Comparator<Photo> comparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
        return res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
    };
    private Album album;
    private Photo[] photos;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        album = new AlbumImpl(capacity);
        photos = new Photo[capacity];
        photos[0] = new Photo(1, 1, "Title1", "url1", now.minusDays(7));
        photos[1] = new Photo(1, 2, "Title2", "url2", now.minusDays(6));
        photos[2] = new Photo(1, 3, "Title3", "url3", now.minusDays(5));
        photos[3] = new Photo(2, 1, "Title1", "url1", now.minusDays(4));
        photos[4] = new Photo(2, 4, "Title4", "url4", now.minusDays(3));
        photos[5] = new Photo(1, 4, "Title4", "url4", now.minusDays(2));
        for (int i = 0; i < photos.length - 1; i++) {
            album.addPhoto(photos[i]);
        }
    }

    @org.junit.jupiter.api.Test
    void testAddPhoto() {
        assertFalse(album.addPhoto(null));
        assertFalse(album.addPhoto(photos[2]));
        assertTrue(album.addPhoto(photos[5]));
        assertEquals(capacity, album.size());
        assertFalse(album.addPhoto(new Photo(10, 4, "Title4", "url4", now.minusDays(2))));
    }

    @org.junit.jupiter.api.Test
    void testRemovePhoto() {
        assertFalse(album.removePhoto(4, 1));
        assertTrue(album.removePhoto(1, 1));
        assertEquals(4, album.size());
    }

    @org.junit.jupiter.api.Test
    void testUpdatePhoto() {
        assertTrue(album.updatePhoto(2, 1, "newUrl"));
        assertEquals("newUrl", album.getPhotoFromAlbum(2, 1).getUrl());

    }

    @org.junit.jupiter.api.Test
    void testGetPhotoFromAlbum() {
        assertEquals(photos[0], album.getPhotoFromAlbum(1, 1));
        assertNull(album.getPhotoFromAlbum(4, 1));
    }

    @org.junit.jupiter.api.Test
    void testGetAllPhotoFromAlbum() {
        Photo[] actual = album.getAllPhotoFromAlbum(2); // { photos[4], photos[3] }
        Arrays.sort(actual, comparator);
        Photo[] expected = {photos[3], photos[4]};
        assertArrayEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void testGetPhotoBetweenDate() {
        LocalDate localDate = LocalDate.now();
        Photo[] actual = album.getPhotoBetweenDate(localDate.minusDays(6), localDate.minusDays(3));
        Arrays.sort(actual, comparator);
        Photo[] expected = {photos[1], photos[2], photos[3], photos[4]};
        assertArrayEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void testSize() {
        assertEquals(capacity - 1, album.size());
    }
}