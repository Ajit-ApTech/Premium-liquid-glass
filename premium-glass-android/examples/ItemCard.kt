package x
@Composable fun ItemCard() {
  GlassSurface(hazeState, tier) {
     Text("a", fontSize = 14.sp, color = Color(0xFF112233))
     GlassSurface(hazeState, tier) { Text("nested") }
  }
  Box(Modifier.blur(8.dp)) { Text("x") }
  SwipeToDismissBox(state = s, backgroundContent = {}) { }
}
