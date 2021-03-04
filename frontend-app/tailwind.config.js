module.exports = {
  purge: [],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {},
    screens: {
	xl: { max: "1279px"},
	lg: { max: "1023px"},
	md: { max: "767px"},
	sm: { max: "639px"}
    }
  },
  variants: {
    extend: {},
  },
  plugins: [],
}
