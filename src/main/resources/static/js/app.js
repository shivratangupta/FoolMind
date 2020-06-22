window.app = new Vue({
    el: '#app',
    data: {
        playerAlias: "",
        gameModes: [],
        numRounds: 10,
        hasBot: false,
        currentGame: null,
        gameState: {}
    },
    methods: {
        createGame: function(mode) {
            fetch('/play/create-game?' + new URLSearchParams({
                mode: mode,
                rounds: this.numRounds,
                bot: this.hasBot
            })).then(this.updateDataFromResponse);
        },

        updateDataFromResponse: function(response) {
            let self = this;
            response.json().then(function(data) {
                console.log(data);
                for(let key in data) {
                    self[key] = data[key];
                }
            })
        }
    },
    mounted: function() {
        fetch('/play/').then(this.updateDataFromResponse);
    }
});