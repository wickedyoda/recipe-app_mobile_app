import SwiftUI

struct ContentView: View {
    @AppStorage("saved_host") private var savedHost: String = ""
    @State private var showPrompt = false
    @State private var currentURL: URL?
    
    var body: some View {
        NavigationView {
            Group {
                if let url = currentURL {
                    RecipeAppWebView(url: url)
                        .edgesIgnoringSafeArea(.all)
                        .toolbarHidden()
                        .overlay(
                            VStack {
                                Spacer()
                                VersionBadge()
                            }
                        )
                } else {
                    VStack(spacing: 20) {
                        Text("WiskFul")
                            .font(.largeTitle)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                        
                        Button(action: { showPrompt = true }) {
                            Text(savedHost.isEmpty ? "Enter Server URL" : "Connect to \(savedHost)")
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color(red: 0x11/255, green: 0x11/255, blue: 0x11/255))
                                .cornerRadius(12)
                        }
                    }
                    .padding()
                    .background(Color(red: 0x11/255, green: 0x11/255, blue: 0x11/255).ignoresSafeArea())
                    .navigationTitle("WiskFul")
                }
            }
        }
        .onAppear {
            if savedHost.isNotEmpty {
                if validateHost(savedHost) {
                    let urlString = savedHost.hasPrefix("http") ? savedHost : "https://\(savedHost)"
                    currentURL = URL(string: urlString)
                } else {
                    savedHost = ""
                    showPrompt = true
                }
            } else {
                showPrompt = true
            }
        }
        .sheet(isPresented: $showPrompt) {
            HostPromptView { host in
                savedHost = host
                let urlString = host.hasPrefix("http") ? host : "https://\(host)"
                currentURL = URL(string: urlString)
            }
        }
    }
    
    private func validateHost(_ host: String) -> Bool {
        let urlString = host.hasPrefix("http") ? host : "https://\(host)"
        guard let url = URL(string: urlString) else { return false }
        guard url.scheme == "https" else { return false }
        return url.host != nil
    }
}

struct VersionBadge: View {
    var versionName: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "unknown"
    }
    
    var body: some View {
        Text("WiskFul \(versionName)")
            .font(.caption2)
            .foregroundColor(.white)
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
            .background(Color.black.opacity(0.7))
            .cornerRadius(8)
            .padding(.bottom, 16)
    }
}

struct HostPromptView: View {
    @Environment(\.dismiss) var dismiss
    @State private var hostInput = ""
    @State private var showAlert = false
    
    var onSave: (String) -> Void
    
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Enter Server Address")
                    .font(.headline)
                    .foregroundColor(.primary)
                
                TextField("https://192.168.1.100:3000", text: $hostInput)
                    .keyboardType(.URL)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                
                Button("Connect") {
                    let trimmed = hostInput.trimmingCharacters(in: .whitespaces)
                    if trimmed.isEmpty {
                        showAlert = true
                        return
                    }
                    onSave(trimmed)
                    dismiss()
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color(red: 0x11/255, green: 0x11/255, blue: 0x11/255))
                .cornerRadius(12)
            }
            .padding()
            .navigationTitle("WiskFul")
            .navigationBarTitleDisplayMode(.inline)
            .alert("Invalid URL", isPresented: $showAlert) {
                Button("OK", role: .cancel) { }
            } message: {
                Text("Please enter a valid HTTPS URL (e.g. https://your-server:3000)")
            }
        }
    }
}