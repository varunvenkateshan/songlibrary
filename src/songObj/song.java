/**
 * Created by Varun Venkateshan and Yashwant Balaji
 * */

package songObj;

import java.util.Objects;
public class song {
        private String title;
        private String artist;
        private String album;
        private String year;

        //Constructor
        public song(String title, String artist, String album, String year) {
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.year = year;
        }

        //Getter Methods
        public String getTitleAndArtist() {
            return title + " by " + artist;
        }
        public String getTitle(){
            return title;
        }
        public String getArtist(){
            return artist;
        }
        public String getAlbum(){
            return album;
        }

        public String getYear() {
            return year;
        }

        //Setter Methods
        public void setTitle(String title) {
            this.title = title;
        }


        public void setArtist(String artist) {
            this.artist = artist;
        }


        public void setAlbum(String album) {
            this.album = album;
        }


        public void setYear(String year) {
            this.year = year;
        }


        //Override equals method to compare songs
        @Override
        public boolean equals(Object song) {
            if (this == song) {
                return true;
            }
            if (song == null || getClass() != song.getClass()) {
                return false;
            }
            song that = (song) song;
            return title.equals(that.title) && artist.equals(that.artist);
        }

        //Override hashCode method
        @Override
        public int hashCode() {
            return Objects.hash(title, artist);
        }
    }
