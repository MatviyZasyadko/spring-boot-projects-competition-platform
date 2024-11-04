/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors');

module.exports = {
  content: [
    '../resources/templates/**/*.{html,js}',
    './node_modules/flowbite/**/*.js',
  ],
  theme: {
    extend: {
      colors: {
        black: colors.black,
        white: colors.white,
        gray: colors.gray,
      },
      fontFamily: {
        palanquin: ['Palanquin', 'sans-serif'],
      },
    },
  },
  plugins: [
    require('flowbite/plugin')({
      charts: true,
    }),
  ],
};

