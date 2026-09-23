<script setup>
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
const toggleDark = useToggle(isDark)
</script>
<template>
  {{ isDark }}
  <br />
  <button @click="toggleDark()">dack</button>
</template>
<style>
.dark {
  background: black;
  color: white;
}
</style>
