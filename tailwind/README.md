# Tailwind CSS build (design source)

`src/main/resources/static/css/commutemate.css` is **generated** from `entry.css`
(the design tokens ported from the original prototype). Only rebuild when you
add/remove Tailwind classes in the templates:

```bash
npm install tailwindcss @tailwindcss/cli tw-animate-css
npx @tailwindcss/cli -i tailwind/entry.css -o src/main/resources/static/css/commutemate.css
```

The `@source` paths in `entry.css` are relative to this folder, so the command
above works from the project root as-is.
