window.app = new Vue({
    el: '#app',
    data: {
        page: "play",
        player: {
            alias: "",
            picURL: "",
            foolFaceURL: "",
            email: "",
            currentGameId: null
        },
        gameModes: [
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            },
            {
                title: "",
                image: "",
                description: ""
            }
        ],
        leaderboard: [
            {
                alias: "",
                picURL: "",
                correctAnswerCount: 0,
                gotFooledCount: 0,
                fooledOthersCount: 0
            }
        ],
        gameState: {
            id: null,
            secretCode: "",
            numRounds: 10,
            gameMode: "",
            hasBot: false,
            status: "",
            round: ""
        },
        createGameData: {
            hasBot: false,
            numRounds: 10
        },
        joinGameData: {
            secretCode: ""
        },
        profileEditData: {
            alias: "",
            picURL: "",
            fooledFaceURL: "",
            email: ""
        },
        errorText: ""
    },
    methods: {
        fetchGameModes: function() {
            fetch('/play/game-modes')
                .then(response => response.json())
                .then(gameModes => {
                    this.gameModes = gameModes;
                });
        },
        fetchPlayerData: function() {
            fetch('/play/player-data')
                .then(response => response.json())
                .then(playerData => {
                    this.player = playerData;
                    this.profileEditData.fooledFaceURL = playerData.fooledFaceURL;
                    this.profileEditData.alias = playerData.alias;
                    this.profileEditData.picURL = playerData.picURL;
                    this.profileEditData.email = playerData.email;
                });
        },
        fetchGameState: function() {
            fetch('/play/game-state')
                .then(response => response.json())
                .then(gameState => {
                    this.gameState = gameState;
                });
        },
        fetchLeaderboard: function() {
            fetch('/play/leaderboard')
                .then(response => response.json())
                .then(leaderboard => {
                    this.leaderboard = leaderboard;
                });
        },
        setError: function(response) {
            if(response.status==='success') return;
            this.errorText = "Error: " + response.errorText;
            let self = this;
            setTimeout(() => {
                self.errorText = '';
            }, 2000);
        },
        updateProfileData: function() {
            fetch('/play/update-profile?' + new URLSearchParams({
                alias: this.profileEditData.alias,
                email: this.profileEditData.email,
                picURL: this.profileEditData.picURL,
                fooledFaceURL: this.profileEditData.fooledFaceURL
            })).then(this.setError).then(this.fetchPlayerData);
        },
        calculateScore: function(stats) {
            return stats.correctAnswerCount * 2 + stats.fooledOthersCount - stats.gotFooledCount;
        },
        createGame: function(gameMode) {
            fetch('/play/create-game?' + new URLSearchParams({
                gameMode: gameMode,
                numRounds: this.createGameData.numRounds,
                hasBot: this.createGameData.hasBot
            })).then(this.setError).then(this.fetchPlayerData).then(this.fetchGameState);
        },
        joinGame: function(gameMode) {
            fetch('/play/join-game?' + new URLSearchParams({
                secretCode: this.joinGameData.secretCode
            })).then(this.setError).then(this.fetchPlayerData).then(this.fetchGameState);
        }
    },
    mounted: function() {
        this.fetchGameState();
        this.fetchGameModes();
        this.fetchPlayerData();
        this.fetchLeaderboard();
        document.getElementById("app").classList.remove("invisible");
    }
});