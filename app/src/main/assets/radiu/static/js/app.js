var audio = new Audio('static/etc/calicomp_startup.mp3');
var filenames = ['tenor.mp4', 'tumblr_opnks5eWY81w2tequo1_500.mp4', 'va11halla.mp4', 'doze.mp4', 'doze8.mp4', 'e843f5e4507d83ef51df7fb27b4d0bb2.mp4', 'doze9.mp4', 'back.mp4', 'doze6.mp4', 'tumblr_or4na0hzyq1w7cvmoo1_400.mp4', 'doze5.mp4', 'aesthetic_anime.mp4', 'city.mp4', 'doze3.mp4', 'tumblr_oul9tv2iCp1vhvnzyo1_500.mp4', 'tumblr_osneazEg8P1wtuf8mo1_500.mp4', 'large.mp4', 'doze7.mp4', 'contact.mp4', 'doze2.mp4', 'doze4.mp4', 'original.mp4', '8ed7f59623c682eebd51f6c10b9d2dee.mp4', 'tumblr_nk8lr1LlAq1s0hk4xo1_500.mp4', '68b211810e941c519d868e8ee9359d7b.mp4'];
$(document).ready(function () {
  var video = document.getElementById("video");
  var src = document.createElement("source");
  src.setAttribute("type", "video/mp4");
  src.setAttribute("src", "static/mp4/" + filenames[Math.floor(Math.random() * filenames.length)]);
  video.appendChild(src);
  //<source src="static/mp4/tumblr_nk8lr1LlAq1s0hk4xo1_500.mp4" type="video/mp4">
  video.play();
  if(radiu.hasWelcomePlayed().toUpperCase != "TRUE") {
    audio.play();
    radiu.setWelcomePlayed(true);
  }
  // we're going to update our html elements / player every 15 seconds
  setInterval(radioTitle, 15000);
  // and also once now
  radioTitle();
  $('#vol_slider').on("change", function() {
      console.log($(this).val());
      //document.getElementById('track').volume = $(this).val() / 100;
      radiu.set_volume(($(this).val() / 100).toString());
  });
  updatePlayerState();
});


var radioTitle = function radioTitle() {
  // this is the URL of the json.xml file located on your server.
  var url = 'http://radio.dangeru.us:8000/json.xsl';
  // this is your mountpoint's name, mine is called /radio
  var mountpoint = '/stream.ogg';

  $.ajax({  type: 'GET',
        url: url,
        async: true,
        jsonpCallback: 'parseMusic',
        contentType: "application/json",
        dataType: 'jsonp',
        success: function (json) {
          // this is the element we're updating that will hold the track title
          $('#track-title').text(json[mountpoint].title + ".ogg");
          document.title = "radi/u/ - " + json[mountpoint].title;
          // this is the element we're updating that will hold the listeners count
          $('#listeners').text("listening_" + json[mountpoint].listeners + ".rtf");
          radiu.update_notif(json[mountpoint].title, json[mountpoint].listeners);
        },
        error: function (e) {
          //console.log(e.message);
        }
  });
}
function togglePlayer() {
  if(document.getElementById("player").style.display == "none") {
    document.getElementById("player").style.display = "block";
  }
  else {
    document.getElementById("player").style.display = "none";
  }
}
function updatePlayerState() {
  if (radiu.is_playing().toUpperCase() == "TRUE") {
    document.getElementById('is-playing').innerHTML = "stop.exe";
  } else {
    document.getElementById('is-playing').innerHTML = "play.exe";
  }
}
function switchPlayerState() {
  if (radiu.is_playing().toUpperCase() == "TRUE") {
    radiu.pause_stream();
  } else {
    audio.pause();
    radiu.play_stream();
  }
  updatePlayerState()
}
