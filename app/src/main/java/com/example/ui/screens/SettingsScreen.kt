package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.TodoViewModel

@Composable
fun SettingsScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Confirmation dialog states
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showImportConfirm by remember { mutableStateOf(false) }

    // Interactive toggles (local applet states)
    var notificationsEnabled by remember { mutableStateOf(true) }
    var highContrastEnabled by remember { mutableStateOf(false) }
    var syncCloudEnabled by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF7FF))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "System Preferences",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1D1B20)
                    )
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF6750A4),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFEF7FF))
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Member/Profile card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEADDFF)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = Color(0xFF6750A4),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Premium Member",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF21005D),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Personal Organizer",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF21005D)
                            )
                        )
                        Text(
                            text = "Local Sandboxed Mode",
                            fontSize = 11.sp,
                            color = Color(0xFF49454F)
                        )
                    }
                }
            }

            Text(
                text = "Preferences",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            // Settings options items list
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFCAC4D0).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Item 1: Push notifications
                    SettingsToggleRow(
                        icon = Icons.Default.NotificationsActive,
                        title = "Push Notifications",
                        subtitle = "Notify me when a high-priority task is nearing its due hour",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color(0xFFCAC4D0).copy(alpha = 0.4f)
                    )

                    // Item 2: Theme customization
                    SettingsToggleRow(
                        icon = Icons.Default.Palette,
                        title = "High Contrast Mode",
                        subtitle = "Enable pure solid colors and deeper borders for increased reading accessibility",
                        checked = highContrastEnabled,
                        onCheckedChange = { highContrastEnabled = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color(0xFFCAC4D0).copy(alpha = 0.4f)
                    )

                    // Item 3: Auto-sync cloud simulation
                    SettingsToggleRow(
                        icon = Icons.Default.Security,
                        title = "Cloud Synchronization",
                        subtitle = "Simulate syncing task milestones with database nodes concurrently",
                        checked = syncCloudEnabled,
                        onCheckedChange = { syncCloudEnabled = it }
                    )
                }
            }

            Text(
                text = "Developer Testing Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            // Database seeding/cleaning cards (highly valuable for user validation)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showImportConfirm = true }
                        .border(1.dp, Color(0xFF81C784), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "Seed Sample Data",
                            tint = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "Seed Samples",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "Add pre-arranged calendar events and stats entries.",
                            fontSize = 11.sp,
                            color = Color(0xFF2E7D32).copy(alpha = 0.8f)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDeleteConfirm = true }
                        .border(1.dp, Color(0xFFE57373), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Purge Database",
                            tint = Color(0xFFC62828)
                        )
                        Text(
                            text = "Clear Database",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "Completely wipe all task logs and details securely.",
                            fontSize = 11.sp,
                            color = Color(0xFFC62828).copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF6750A4)
                    )
                    Column {
                        Text(
                            text = "Modern Android Development (MAD)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1D1B20)
                        )
                        Text(
                            text = "Utilizing Compose ViewPager, Koin Dependency Injector, Room Persistence, Type-Safe Navigation and Edge-to-Edge scaffolding.",
                            fontSize = 11.sp,
                            color = Color(0xFF49454F)
                        )
                    }
                }
            }

            // Build specifications specs
            Text(
                text = "Version 2.0.4 (Compose Stable Build)\nPlatform ID: AIS-Applet-Active",
                fontSize = 10.sp,
                color = Color.Gray,
                lineHeight = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    // Confirmation dialogs
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Confirm Clear Actions", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently erase all task database records? This action is immediate and non-reversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllTodos()
                        showDeleteConfirm = false
                        Toast.makeText(context, "Database cleared successfully!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("YES, WIPE", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("CANCEL")
                }
            }
        )
    }

    if (showImportConfirm) {
        AlertDialog(
            onDismissRequest = { showImportConfirm = false },
            title = { Text("Load Sample Data", fontWeight = FontWeight.Bold) },
            text = { Text("We will import 5 pre-arranged calendar agendas, completed schedules, and metrics entries for instant visualization. Would you like to proceed?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.importSampleData()
                        showImportConfirm = false
                        Toast.makeText(context, "Sample datasets successfully seeded!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("YES, IMPORT", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportConfirm = false }) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6750A4),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.Gray,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF6750A4),
                uncheckedThumbColor = Color(0xFF79747E),
                uncheckedTrackColor = Color(0xFFF7F2FA)
            )
        )
    }
}
